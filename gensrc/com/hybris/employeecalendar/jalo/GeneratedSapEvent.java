/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 10-Feb-2016 11:35:39                        ---
 * ----------------------------------------------------------------
 */
package com.hybris.employeecalendar.jalo;

import com.hybris.employeecalendar.constants.EmployeecalendarConstants;
import com.hybris.employeecalendar.jalo.SapEmployee;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SapEvent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSapEvent extends GenericItem
{
	/** Qualifier of the <code>SapEvent.type</code> attribute **/
	public static final String TYPE = "type";
	/** Qualifier of the <code>SapEvent.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>SapEvent.fromDate</code> attribute **/
	public static final String FROMDATE = "fromDate";
	/** Qualifier of the <code>SapEvent.toDate</code> attribute **/
	public static final String TODATE = "toDate";
	/** Qualifier of the <code>SapEvent.employee</code> attribute **/
	public static final String EMPLOYEE = "employee";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n EMPLOYEE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSapEvent> EMPLOYEEHANDLER = new BidirectionalOneToManyHandler<GeneratedSapEvent>(
	EmployeecalendarConstants.TC.SAPEVENT,
	false,
	"employee",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(TYPE, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(FROMDATE, AttributeMode.INITIAL);
		tmp.put(TODATE, AttributeMode.INITIAL);
		tmp.put(EMPLOYEE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		EMPLOYEEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.description</code> attribute.
	 * @return the description - description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.description</code> attribute.
	 * @return the description - description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.description</code> attribute. 
	 * @param value the description - description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.description</code> attribute. 
	 * @param value the description - description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.employee</code> attribute.
	 * @return the employee
	 */
	public SapEmployee getEmployee(final SessionContext ctx)
	{
		return (SapEmployee)getProperty( ctx, EMPLOYEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.employee</code> attribute.
	 * @return the employee
	 */
	public SapEmployee getEmployee()
	{
		return getEmployee( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.employee</code> attribute. 
	 * @param value the employee
	 */
	public void setEmployee(final SessionContext ctx, final SapEmployee value)
	{
		EMPLOYEEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.employee</code> attribute. 
	 * @param value the employee
	 */
	public void setEmployee(final SapEmployee value)
	{
		setEmployee( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.fromDate</code> attribute.
	 * @return the fromDate - from date
	 */
	public Date getFromDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, FROMDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.fromDate</code> attribute.
	 * @return the fromDate - from date
	 */
	public Date getFromDate()
	{
		return getFromDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.fromDate</code> attribute. 
	 * @param value the fromDate - from date
	 */
	public void setFromDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, FROMDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.fromDate</code> attribute. 
	 * @param value the fromDate - from date
	 */
	public void setFromDate(final Date value)
	{
		setFromDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.toDate</code> attribute.
	 * @return the toDate - to date
	 */
	public Date getToDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, TODATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.toDate</code> attribute.
	 * @return the toDate - to date
	 */
	public Date getToDate()
	{
		return getToDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.toDate</code> attribute. 
	 * @param value the toDate - to date
	 */
	public void setToDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, TODATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.toDate</code> attribute. 
	 * @param value the toDate - to date
	 */
	public void setToDate(final Date value)
	{
		setToDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.type</code> attribute.
	 * @return the type - type event
	 */
	public EnumerationValue getType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEvent.type</code> attribute.
	 * @return the type - type event
	 */
	public EnumerationValue getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.type</code> attribute. 
	 * @param value the type - type event
	 */
	public void setType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEvent.type</code> attribute. 
	 * @param value the type - type event
	 */
	public void setType(final EnumerationValue value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
}
