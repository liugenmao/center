package ${domainPackage};

import java.util.Date;
import java.math.BigDecimal;

/**
 *
 *
 * @author
 * @version
 **/
public class ${domainName} {
	
	<#list fields as field>
	/**
	 *
	 **/
	private ${fieldDataTypes[field]} ${field};
	</#list>
	
}