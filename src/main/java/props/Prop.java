package props;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Prop<T>
{
    private String uuid = UUID.randomUUID().toString();
    private transient Object parent;
    private T value;
    private boolean set;
    private long timestamp;
    private String modifiedBy;
    
    private boolean recordChanges;
    private List<Version<T>> versions;
    
    private transient List<PropListener> changeListeners;
    
    /**
     * Creates a new Prop with no initial value or parent, and record changes enabled.
     */
    public Prop(){
        this( null );
    }
    
    /**
     * Creates a new Prop with an initial value, no parent, and record changes enabled.
     * @param value the initial value. If this is null, set() will return false
     */
    public Prop(T value){
        this( null, value );
    }
    
    /**
     * Creates a new Prop with a parent, an initial value, and record changes enabled.
     * @param parent the parent object
     * @param value the initial value. If this is null, set() will return false
     */
    public Prop(Object parent, T value){
        this( parent, value, true );
    }
    
    /**
     * Constructs a new Prop instance.
     * @param parent the parent object of this property.
     * @param value the initial value of the property. If this is null, set() 
     *              will return false
     * @param recordChanges if true, this property will make a historical
     *                      record of modifications, including values,
     *                      the user making the change, and the timestamp.
     */
    public Prop( Object parent, T value, boolean recordChanges ){
        this( parent, value, recordChanges, -1, null );
    }

    /**
     * Constructs a new Prop instance.
     * @param parent the parent object of this property.
     * @param value the initial value of the property. If this is null, set() 
     *              will return false
     * @param recordChanges if true, this property will make a historical
     *                      record of modifications, including values,
     *                      the user making the change, and the timestamp.
     * @param timestamp the timestamp to record for this version
     * @param modifiedBy the modifying user's ID to record for this version
     */
    public Prop( Object parent, T value, boolean recordChanges, long timestamp, String modifiedBy ) {
        uuid = UUID.randomUUID().toString();
        
        this.parent = parent;
        this.timestamp = timestamp;
        this.modifiedBy = modifiedBy;
            
        if( value != null ){
            set( value, modifiedBy, timestamp );
        }
        
        this.recordChanges = recordChanges;
        if( recordChanges ){
            versions = new ArrayList();
        }
        
        changeListeners = new ArrayList();
    }
    
    /**
     * Returns the current value of the property.
     * @return the current value of the property.
     */
    public T get(){
        return value;
    }

    /**
     * Returns the Unix-formatted timestamp of the most recent modification,
     * or -1 if the property has not been set.
     * @return  the timestamp of the most recent modification.
     */
    public long timestamp(){
        return timestamp;
    }

    /**
     * Returns an identifier of the person responsible for the latest
     * version of this property.
     * @return the user's identifier
     */
    public String modifiedBy(){
        return modifiedBy;
    }
    
    /**
     * Returns true if the property has been set, false otherwise.
     * @return true if the property has been set.
     */
    public boolean set(){
        return set;
    }
    
    /**
     * Sets the value of the property. This will cause set() to return true,
     * update timestamp(), notify any listeners, and record a version if 
     * record changes is enabled.
     * @param value The new value of the property.
     */
    public void set(T value ){
        set( value, null );
    }
    
    /**
     * Sets the value of the property. The value of modifiedBy will be recorded
     * in the historical version that is created, if record changes is enabled.
     * @param value the new value of the property
     * @param modifiedBy an identifier for the person who made this modification
     */
    public void set(T value, String modifiedBy ){
        set( value, modifiedBy, new Date().getTime() );
    }
    
    /**
     * Sets the value of the property. The value of timestamp will be
     * recorded, rather than the current time.
     * @param value the new value of the property
     * @param timestamp the timestamp of the modification
     */
    public void set(T value, long timestamp ){
        set( value, null, timestamp );
    }
    
    /**
     * Sets the value of the property. This will cause set() to return true,
     * update timestamp(), notify any listeners, and record a version if 
     * record changes is enabled.
     * @param newValue the new value of the property
     * @param modifiedBy an identifier for the person who made this modification
     * @param timestamp the timestamp of the modification
     */
    public void set(T newValue, String modifiedBy, long timestamp ){
        if( recordChanges && set ){
            Version<T> previous = new Version( this.value, this.timestamp, this.modifiedBy );
            versions.add( previous );
        }
        
        T oldValue = this.value;
        this.value = newValue;
        this.set = true;
        this.timestamp = timestamp;
        this.modifiedBy = modifiedBy;
        
        changed( oldValue, newValue );
    }
    
    /**
     * Adds a listener to this property.
     * @param listener the listener
     */
    public void addListener( PropListener listener ){
        changeListeners.add( listener );
    }
    
    /**
     * Removes a listener to this property.
     * @param listener the listener
     */
    public void removeListener( PropListener listener ){
        changeListeners.remove( listener );
    }
    
    /**
     * Removes all listeners from this property.
     */
    public void removeListeners(){
        changeListeners.clear();
    }
    
    private void changed( T oldValue, T newValue ){
        if( changeListeners != null && !changeListeners.isEmpty() ){
            PropChangedEvent event = new PropChangedEvent( this, newValue, oldValue );
            
            List<PropListener> toNotify = new ArrayList();
            toNotify.addAll( changeListeners );
            Collections.reverse( toNotify );
            
            for( PropListener listener : toNotify ){
                listener.propertyChanged( event );
            }
        }
    }
    
    /**
     * Returns a list of previous versions of this property.
     * @return a list of previous versions of this property.
     */
    public List<Version<T>> versions(){
        if( recordChanges ){
            List<Version<T>> result = new ArrayList();
            
            if( versions != null && ! versions.isEmpty() ){
                result.addAll( versions );
            }
            
            return result;
        }
        else{
            return null;
        }
    }
    
    /**
     * Returns the unique identifier of this property.
     * @return the unique identifier of this property.
     */
    public String uuid() {
        return uuid;
    }

    /**
     * Sets the unique identifier of this property. This does not fire a
     * PropChangedEvent or record a historical version of this
     * property.
     * @param uuid the unique identifier of this property.
     */
    public void uuid( String uuid ) {
        this.uuid = uuid;
    }

    /**
     * Returns the parent object of this property.
     * @return the parent object of this property.
     */
    public Object parent() {
        return parent;
    }

    /**
     * Sets the parent object of this property. This does not fire a
     * PropChangedEvent or record a historical version of this
     * property.
     * @param parent the parent object of this property.
     */
    public void parent( Object parent ) {
        this.parent = parent;
    }
    
}
