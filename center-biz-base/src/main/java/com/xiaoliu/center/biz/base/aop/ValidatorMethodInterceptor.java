package com.xiaoliu.center.biz.base.aop;

import com.xiaoliu.center.biz.base.annotation.*;
import com.xiaoliu.center.common.exception.ValidationException;
import com.xiaoliu.center.common.result.Result;
import com.xiaoliu.center.common.utils.ValidatorUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 方法参数验证
 * <p>
 * <pre class="code">
 * Example :
 *
 * @NotNull( name = { "username", "password" }, messages = { "xxx", "yyy" } )
 * @NotEmpty( name = "roles", messages = { "xxx" } )
 * @Length( name = { "username", "password" }, min = 6, max = 20, messages = { "xxx", "yyy" } )
 * @Max( name = "age", value = 100, messages = "xxx" )
 * @Min( name = "age", value = 10,  messages = "xxx" )
 * public Result addUser ( BuUser buUser ) {
 * // 干点儿什么业务逻辑之类的事儿
 * }
 * <p>
 * </pre>
 */
public class ValidatorMethodInterceptor implements MethodInterceptor {

    private Logger LOG = LoggerFactory.getLogger(ValidatorMethodInterceptor.class);

    private static final String SUCCESS = "success";

    /**
     * 参数对象深度分割字符
     *
     * @since 1.2
     */
    private static final String PARAM_NAME_DEPTH_DELIMITER = "\\.";

    /**
     * 消息资源
     */
    @SuppressWarnings("unused")
    @Deprecated
    private MessageSource messageSource;

    /**
     * 验证消息资源
     *
     * @since 1.2
     */
    private List<MessageSource> messageSources;

    /**
     * 消息资源名称
     *
     * @since 1.2
     */
    private String[] messageSourceBeanNames;

    private static ClassPool CLASS_POOL = ClassPool.getDefault();

    static {
        CLASS_POOL.insertClassPath(new ClassClassPath(ValidatorMethodInterceptor.class));
    }

    public ValidatorMethodInterceptor() {
        messageSources = new ArrayList<MessageSource>();
    }

    /**
     * 在执行切面之前加载一些必要的Bean文件
     *
     * @since 1.2
     */
    private void postProcessBeanLoad() {
        if (ValidatorUtils.notEmpty(messageSourceBeanNames)) {
            for (String messageSourceBeanName : messageSourceBeanNames) {
                MessageSource messageSource = (MessageSource) SpringApplicationContextHolder.getBean(messageSourceBeanName);
                messageSources.add(messageSource);
            }

            messageSourceBeanNames = new String[]{};
        }
    }

    /**
     * @change 1.0版本中的消息资源只支持单个messageSource，考虑到后期的扩展，将消息资源更改为支持多个
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        postProcessBeanLoad();

        Method method = invocation.getMethod();
        Annotation[] annotations = invocation.getMethod().getAnnotations();
        if (ValidatorUtils.isEmpty(invocation.getMethod().getAnnotations())) {
            Object subObject = invocation.getThis();
            annotations = subObject.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotations();
        }

        LOG.debug("拦截方法{}进行验证", method.getName());

        String[] paramNames = getMethodParameterNames(invocation.getThis(), method);

        String message = SUCCESS;
        String[] messageArgs = new String[]{};
        try {
            if (paramNames != null && annotations != null) {
                for (Annotation annotation : annotations) {
                    /** 对非空注解进行处理 */
                    if (NotNull.class.isInstance(annotation)) {
                        message = handleNotNull((NotNull) annotation, paramNames, invocation.getArguments());
                        messageArgs = ((NotNull) annotation).messageArgs();
                    }
                    /** 对集合或数组的非空进行处理 */
                    else if (NotEmpty.class.isInstance(annotation)) {
                        message = handleNotEmpty((NotEmpty) annotation, paramNames, invocation.getArguments());
                        messageArgs = ((NotEmpty) annotation).messageArgs();
                    }
                    /** 对数字大小的限制处理 */
                    else if (Size.class.isInstance(annotation)) {
                        message = handleSize((Size) annotation, paramNames, invocation.getArguments());
                        messageArgs = ((Size) annotation).messageArgs();
                    }
                    /** 对长度限制处理 */
                    else if (Length.class.isInstance(annotation)) {
                        message = handleStringLength((Length) annotation, paramNames, invocation.getArguments());
                        messageArgs = ((Length) annotation).messageArgs();
                    }
                    /** 对数字最大值的限制处理 */
                    else if (Max.class.isInstance(annotation)) {
                        message = handleNumberMax((Max) annotation, paramNames, invocation.getArguments());
                        messageArgs = ((Max) annotation).messageArgs();
                    }
                    /** 对数字最小值的限制处理 */
                    else if (Min.class.isInstance(annotation)) {
                        message = handleNumberMin((Min) annotation, paramNames, invocation.getArguments());
                        messageArgs = ((Min) annotation).messageArgs();
                    }

                    // 一旦发现有一个校验不通过，立马终止注解的扫描
                    if (!SUCCESS.equals(message)) {
                        break;
                    }
                }
            }
        } catch (ValidationException e) {
            // 处理验证错误异常x
            message = e.getMessage();
        }

        if (SUCCESS.equals(message)) {
            return invocation.proceed();
        }
        // 取得方法执行后的返回值类型
        Class<?> returnType = method.getReturnType();

        if (returnType.isAssignableFrom(Result.class)) {
            return Result.failed(message);
        } else {
            throw new ValidationException(message);
        }
    }

    /**
     * 对数据进行非NULL校验
     *
     * @param annotation {@link NotNull}
     * @param paramNames 方法形参名
     * @param arguments  形参值
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ValidationException
     */
    protected String handleNotNull(NotNull annotation, String[] paramNames, Object[] arguments) throws NotFoundException, IllegalArgumentException, IllegalAccessException,
            ValidationException {
        String[] fields = annotation.name();
        String[] errorMessages = annotation.messages();
        for (int i = 0, len = fields.length; i < len; i++) {
            String field = fields[i];
            Object value = findValueWithAnnotationParameter(field, paramNames, arguments);
            try {
                if (value == null) {
                    return errorMessages[i];
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ValidationException("参数[" + field + "]的值不能为null");
            }
        }
        return SUCCESS;
    }

    /**
     * 对数据进行非空校验
     *
     * @param annotation {@link NotEmpty}
     * @param paramNames 方法形参名
     * @param arguments  形参值
     * @return
     * @throws NotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ValidationException
     */
    protected String handleNotEmpty(NotEmpty annotation, String[] paramNames, Object[] arguments) throws NotFoundException, IllegalArgumentException, IllegalAccessException,
            ValidationException {
        String[] fields = annotation.name();
        String[] errorMessages = annotation.messages();
        if (fields.length > 0) {
            for (int i = 0, len = fields.length; i < len; i++) {
                String field = fields[i];
                Object value = findValueWithAnnotationParameter(field, paramNames, arguments);
                try {
                    if (ValidatorUtils.isEmpty(value)) {
                        return errorMessages[i];
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new ValidationException("参数[" + field + "]的值不能为空");
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 对数字型的参数进行区间校验
     *
     * @param annotation {@link Size}
     * @param paramNames 方法形参名
     * @param arguments  形参值
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws ValidationException
     */
    protected String handleSize(Size annotation, String[] paramNames, Object[] arguments) throws IllegalArgumentException, IllegalAccessException, NotFoundException,
            ValidationException {
        String[] fields = annotation.name();
        String[] errorMessages = annotation.messages();

        float[] mins = annotation.min();
        float[] maxs = annotation.max();
        if (fields.length > 0) {
            for (int i = 0, len = fields.length; i < len; i++) {
                String field = fields[i];
                Object value = findValueWithAnnotationParameter(field, paramNames, arguments);

                // 对未设置长度的数据做默认处理, min = 0, max = Integer.MAX_VALUE
                float min, max;
                try {
                    min = mins[i];
                } catch (IndexOutOfBoundsException e) {
                    min = mins[0];
                }

                try {
                    max = maxs[i];
                } catch (IndexOutOfBoundsException e) {
                    max = maxs[0];
                }

                try {
                    if (value instanceof Number) {
                        if (((Number) value).floatValue() < min || ((Number) value).floatValue() > max) {
                            return errorMessages[i];
                        }
                    } else {
                        throw new IllegalArgumentException("参数[" + field + "]不是数字类型");
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new ValidationException("参数[" + field + "]的值只能在 " + min + "," + max + "之间");
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 对数字的最大值校验
     *
     * @param annotation {@link Max}
     * @param paramNames 方法形参名
     * @param arguments  形参值
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws ValidationException
     */
    protected String handleNumberMax(Max annotation, String[] paramNames, Object[] arguments) throws IllegalArgumentException, IllegalAccessException, NotFoundException,
            ValidationException {
        String[] fields = annotation.name();
        String[] errorMessages = annotation.messages();

        float[] maxValues = annotation.value();
        if (fields.length > 0) {
            for (int i = 0, len = fields.length; i < len; i++) {
                String field = fields[i];
                Object value = findValueWithAnnotationParameter(field, paramNames, arguments);

                // 对未设置最大值做默认处理, max = Float.MAX_VALUE;
                float max;
                try {
                    max = maxValues[i];
                } catch (IndexOutOfBoundsException e) {
                    max = maxValues[0];
                }

                try {
                    if (value instanceof Number) {
                        if (((Number) value).floatValue() > max) {
                            return errorMessages[i];
                        }
                    } else {
                        throw new IllegalArgumentException("参数[" + field + "]不是数字类型");
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new ValidationException("参数[" + field + "]的值不能大于" + max);
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 判断参数的最小值
     *
     * @param annotation
     * @param paramNames
     * @param arguments
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws ValidationException
     */
    protected String handleNumberMin(Min annotation, String[] paramNames, Object[] arguments) throws IllegalArgumentException, IllegalAccessException, NotFoundException,
            ValidationException {
        String[] fields = annotation.name();
        String[] errorMessages = annotation.messages();

        float minValues[] = annotation.value();
        if (fields.length > 0) {
            for (int i = 0, len = fields.length; i < len; i++) {
                String field = fields[i];
                Object value = findValueWithAnnotationParameter(field, paramNames, arguments);

                // 对未设置最小值做默认处理, min = Float.MAX_VALUE;
                float min;
                try {
                    min = minValues[i];
                } catch (IndexOutOfBoundsException e) {
                    min = minValues[0];
                }

                try {
                    if (value instanceof Number) {
                        if (((Number) value).floatValue() < min) {
                            return errorMessages[i];
                        }
                    } else {
                        throw new IllegalArgumentException("参数[" + field + "]不是数字类型");
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new ValidationException("参数[" + field + "]的值不能小于" + min);
                }
            }
        }
        return SUCCESS;
    }

    protected String handleStringLength(Length annotation, String[] paramNames, Object[] arguments) throws IllegalArgumentException, IllegalAccessException, NotFoundException,
            ValidationException {
        String[] fields = annotation.name();
        String[] errorMessages = annotation.messages();

        int[] mins = annotation.min();
        int[] maxs = annotation.max();
        if (fields.length > 0) {
            for (int i = 0, len = fields.length; i < len; i++) {
                String field = fields[i];
                Object value = findValueWithAnnotationParameter(field, paramNames, arguments);

                // 对未设置长度的数据做默认处理, min = 0, max = Integer.MAX_VALUE
                int min, max;
                try {
                    min = mins[i];
                } catch (IndexOutOfBoundsException e) {
                    min = mins[0];
                }

                try {
                    max = maxs[i];
                } catch (IndexOutOfBoundsException e) {
                    max = maxs[0];
                }
                try {
                    if (value instanceof String) {
                        int strLen = ((String) value).length();
                        if (strLen < min || strLen > max) {
                            return errorMessages[i];
                        }
                    } else {
                        throw new IllegalArgumentException("参数[" + field + "]不是字符串类型");
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new ValidationException("参数[" + field + "]的长度必须在" + min + "和" + max + "之间");
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 根据注解中指定的参数名，找出指定参数名在方法形参中的值
     *
     * @param validateName 指定参数名
     * @param paramNames   方法形参名
     * @param arguments    形参值
     * @return
     * @throws NotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Object findValueWithAnnotationParameter(String validateName, String[] paramNames, Object[] arguments) throws NotFoundException, IllegalArgumentException,
            IllegalAccessException {
        Assert.notNull(validateName, "需要验证的参数名称不能为null");

        for (int i = 0, len = paramNames.length; i < len; i++) {
            String name = paramNames[i];
            Object arg = arguments[i];

            if (arg != null) {
                if (isPrimitiveDataType(arg.getClass()) || isArrayOrCollectionType(arg)) {
                    if (validateName.equals(name)) {
                        return arg;
                    }
                } else if (isMultipleFieldName(validateName)) {
                    return depthFindClassFieldValue(validateName, name, arg);
                } else {
                    Field field = ReflectionUtils.findField(arg.getClass(), validateName);
                    if (field != null) {
                        ReflectionUtils.makeAccessible(field);
                        return field.get(arg);
                    }
                }
            } else if (validateName.equals(name)) {
                return null;
            }
        }
        throw new NotFoundException("没有找到[" + validateName + "]参数，请确认当前方法参数中是否存在此参数名称");
    }

    /**
     * 获取方法的所有参数名
     *
     * @param subclass
     * @param method
     * @return
     * @throws NotFoundException
     */
    private String[] getMethodParameterNames(Object subclass, Method method) throws NotFoundException {
        method = ReflectionUtils.findMethod(subclass.getClass(), method.getName(), method.getParameterTypes());

        CtClass cc = getCtClass(method.getDeclaringClass());
        CtClass[] parameterCtClasses = new CtClass[method.getParameterTypes().length];
        for (int i = 0; i < parameterCtClasses.length; i++)
            parameterCtClasses[i] = getCtClass(method.getParameterTypes()[i]);

        String[] parameterNames = new String[parameterCtClasses.length];
        CtMethod cm = cc.getDeclaredMethod(method.getName(), parameterCtClasses);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < attr.tableLength(); i++) {
            if (attr.index(i) >= pos && attr.index(i) < parameterNames.length + pos)
                parameterNames[attr.index(i) - pos] = attr.variableName(i);
        }
        LOG.debug("验证取得方法参数名为 ：{}", ToStringBuilder.reflectionToString(parameterNames, ToStringStyle.SHORT_PREFIX_STYLE));

        return parameterNames;
    }

    public static CtClass getCtClass(Class<?> clazz) throws NotFoundException {
        return CLASS_POOL.getCtClass(clazz.getName());
    }

    /**
     * 判断对象是否为基础类型(支持Number、String、Boolean、Date)
     *
     * @param type
     * @return
     */
    protected boolean isPrimitiveDataType(Class<?> type) {
        if (Number.class.isAssignableFrom(type)) {
            return true;
        } else if (String.class.isAssignableFrom(type)) {
            return true;
        } else if (Boolean.class.isAssignableFrom(type)) {
            return true;
        } else if (Date.class.isAssignableFrom(type)) {
            return true;
        } else if (int.class.isAssignableFrom(type)) {
            return true;
        } else if (double.class.isAssignableFrom(type)) {
            return true;
        } else if (float.class.isAssignableFrom(type)) {
            return true;
        } else if (long.class.isAssignableFrom(type)) {
            return true;
        } else if (boolean.class.isAssignableFrom(type)) {
            return true;
        } else if (char.class.isAssignableFrom(type)) {
            return true;
        } else if (byte.class.isAssignableFrom(type)) {
            return true;
        } else if (Number[].class.isAssignableFrom(type)) {
            return true;
        } else if (String[].class.isAssignableFrom(type)) {
            return true;
        } else if (Boolean[].class.isAssignableFrom(type)) {
            return true;
        } else if (Date[].class.isAssignableFrom(type)) {
            return true;
        } else if (Enum.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    @Deprecated
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 设置多个消息资源
     *
     * @param messageSources
     * @since 1.2
     */
    public void addMessageSources(MessageSource... messageSources) {
        for (MessageSource msgSrc : messageSources) {
            if (msgSrc != null) {
                this.messageSources.add(msgSrc);
            }
        }
    }

    public void setMessageSourceBeanNames(String... messageSourceBeanNames) {
        this.messageSourceBeanNames = messageSourceBeanNames;
    }

    /**
     * 判断当前参数名是否为多重数据对象
     *
     * @param objFieldName 参数名称
     * @since 1.2
     */
    protected boolean isMultipleFieldName(String objFieldName) {
        if (ValidatorUtils.isEmpty(objFieldName) || objFieldName.indexOf(".") == -1) {
            return false;
        }
        return true;
    }

    /**
     * 深度查找复杂对象中的参数值，验证中校验复杂对象中的对象
     *
     * @param paramName 参数名 e.g: user.address.xxx
     * @param argument  参数对象
     * @return
     * @throws NotFoundException 当设定的校验参数未找到时抛出
     * @since 1.2
     */
    public Object depthFindClassFieldValue(String paramName, String argumentName, Object argument) throws NotFoundException {
        if (!isMultipleFieldName(paramName)) {
            return null;
        }
        // 将深度的参数名切割成多个名称，这样才能根据不同的参数名去获取不同的对象
        String[] multipleNames = paramName.split(PARAM_NAME_DEPTH_DELIMITER);

        String parsedParamName = new String();
        if (multipleNames[0].equals(argumentName)) {
            parsedParamName = multipleNames[0];
            multipleNames = Arrays.copyOfRange(multipleNames, 1, multipleNames.length);
        }

        for (int i = 0, len = multipleNames.length; i < len; i++) {
            String name = multipleNames[i];
            parsedParamName += ValidatorUtils.isEmpty(parsedParamName) ? "" : "." + name;

            Field field = ReflectionUtils.findField(argument.getClass(), name);
            if (field == null) {
                throw new NotFoundException("没有找到" + parsedParamName + "参数");
            }

            try {
                ReflectionUtils.makeAccessible(field);
                Object value = field.get(argument);
                if (value == null) {
                    return null;
                }

                // 如果获取到的参数值是基础类型、或已经解析到最后个参数后就返回最后解析的参数值
                if (isPrimitiveDataType(value.getClass()) || (i + 1) == len) {
                    return value;
                }
                argument = value;
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException("参数" + parsedParamName + "无法访问");
            }
        }
        return null;
    }

    /**
     * 判断对象是否为数组或集合类型
     *
     * @param arg
     * @return
     * @since 1.3.28
     */
    protected boolean isArrayOrCollectionType(Object arg) {
        if (arg instanceof Collection) {
            return true;
        } else if (arg.getClass().isArray()) {
            return true;
        }
        return false;
    }
}
