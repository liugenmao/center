package ${domainPackage};

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author
 * @date
 **/
public class ${domainName} {
	
	<#list fields as field>
	/**
	 *
	 **/
	private ${fieldDataTypes[field]} ${field};
	</#list>
	
}