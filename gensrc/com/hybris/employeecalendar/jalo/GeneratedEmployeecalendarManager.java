/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 02-Sep-2015 13:42:45                        ---
 * ----------------------------------------------------------------
 */
package com.hybris.employeecalendar.jalo;

import com.hybris.employeecalendar.constants.EmployeecalendarConstants;
import com.hybris.employeecalendar.jalo.SapEmployee;
import com.hybris.employeecalendar.jalo.SapEvent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>EmployeecalendarManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedEmployeecalendarManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public SapEmployee createSapEmployee(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( EmployeecalendarConstants.TC.SAPEMPLOYEE );
			return (SapEmployee)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating SapEmployee : "+e.getMessage(), 0 );
		}
	}
	
	public SapEmployee createSapEmployee(final Map attributeValues)
	{
		return createSapEmployee( getSession().getSessionContext(), attributeValues );
	}
	
	public SapEvent createSapEvent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( EmployeecalendarConstants.TC.SAPEVENT );
			return (SapEvent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating SapEvent : "+e.getMessage(), 0 );
		}
	}
	
	public SapEvent createSapEvent(final Map attributeValues)
	{
		return createSapEvent( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return EmployeecalendarConstants.EXTENSIONNAME;
	}
	
}
