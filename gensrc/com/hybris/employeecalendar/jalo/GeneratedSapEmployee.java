/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 02-Sep-2015 13:42:45                        ---
 * ----------------------------------------------------------------
 */
package com.hybris.employeecalendar.jalo;

import com.hybris.employeecalendar.constants.EmployeecalendarConstants;
import com.hybris.employeecalendar.jalo.SapEvent;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SapEmployee}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSapEmployee extends GenericItem
{
	/** Qualifier of the <code>SapEmployee.inumber</code> attribute **/
	public static final String INUMBER = "inumber";
	/** Qualifier of the <code>SapEmployee.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SapEmployee.surname</code> attribute **/
	public static final String SURNAME = "surname";
	/** Qualifier of the <code>SapEmployee.events</code> attribute **/
	public static final String EVENTS = "events";
	/**
	* {@link OneToManyHandler} for handling 1:n EVENTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SapEvent> EVENTSHANDLER = new OneToManyHandler<SapEvent>(
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
		tmp.put(INUMBER, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(SURNAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.events</code> attribute.
	 * @return the events
	 */
	public Collection<SapEvent> getEvents(final SessionContext ctx)
	{
		return EVENTSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.events</code> attribute.
	 * @return the events
	 */
	public Collection<SapEvent> getEvents()
	{
		return getEvents( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.events</code> attribute. 
	 * @param value the events
	 */
	public void setEvents(final SessionContext ctx, final Collection<SapEvent> value)
	{
		EVENTSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.events</code> attribute. 
	 * @param value the events
	 */
	public void setEvents(final Collection<SapEvent> value)
	{
		setEvents( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to events. 
	 * @param value the item to add to events
	 */
	public void addToEvents(final SessionContext ctx, final SapEvent value)
	{
		EVENTSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to events. 
	 * @param value the item to add to events
	 */
	public void addToEvents(final SapEvent value)
	{
		addToEvents( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from events. 
	 * @param value the item to remove from events
	 */
	public void removeFromEvents(final SessionContext ctx, final SapEvent value)
	{
		EVENTSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from events. 
	 * @param value the item to remove from events
	 */
	public void removeFromEvents(final SapEvent value)
	{
		removeFromEvents( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.inumber</code> attribute.
	 * @return the inumber
	 */
	public String getInumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, INUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.inumber</code> attribute.
	 * @return the inumber
	 */
	public String getInumber()
	{
		return getInumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.inumber</code> attribute. 
	 * @param value the inumber
	 */
	public void setInumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, INUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.inumber</code> attribute. 
	 * @param value the inumber
	 */
	public void setInumber(final String value)
	{
		setInumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.surname</code> attribute.
	 * @return the surname - Surname
	 */
	public String getSurname(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SURNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SapEmployee.surname</code> attribute.
	 * @return the surname - Surname
	 */
	public String getSurname()
	{
		return getSurname( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.surname</code> attribute. 
	 * @param value the surname - Surname
	 */
	public void setSurname(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SURNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SapEmployee.surname</code> attribute. 
	 * @param value the surname - Surname
	 */
	public void setSurname(final String value)
	{
		setSurname( getSession().getSessionContext(), value );
	}
	
}
