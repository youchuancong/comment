package ${package};

import java.util.Date;
import lombok.Data;
import com.ycc.core.jfinal.db.Module; 
import com.ycc.core.util.db.*;

@Data
@RTable("${table.tableName}")
public class ${table.className}  implements Module{
	public static final String TABLE="${table.tableName}";
	
<#list table.columns as c>
	public static final String ${c.name?upper_case}="${c.name}";
</#list>


<#list table.columns as c>
	@RColumn("${c.name}")
	public ${c.javaType} ${c.proName};
</#list>
}