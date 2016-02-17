package props;

public class Version<T>
{
    private final T value;
    private final long timestamp;
    private final String modifiedBy;

    public Version( T value, long timestamp, String modifiedBy ) {
        this.value = value;
        this.timestamp = timestamp;
        this.modifiedBy = modifiedBy;
    }

    public T value() {
        return value;
    }

    public long timestamp() {
        return timestamp;
    }

    public String modifiedBy() {
        return modifiedBy;
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        
        builder.append( "[" );
        builder.append( value );
        builder.append( "]" );
        
        if( timestamp >= 0 ){
            builder.append( " modified on " );
            builder.append( timestamp );
        }
        
        if( modifiedBy != null && !modifiedBy.trim().isEmpty() ){
            builder.append( " by " );
            builder.append( modifiedBy );
        }
        
        return builder.toString();
    }
}
