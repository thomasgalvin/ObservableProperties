package props;

public class PropChangedEvent<T>
{
    private final Prop<T> property;
    private final T value;
    private final T previousValue;

    public PropChangedEvent( Prop<T> property, T value, T previousValue ) {
        this.property = property;
        this.value = value;
        this.previousValue = previousValue;
    }

    public Prop<T> property() {
        return property;
    }

    public T value() {
        return value;
    }

    public T previousValue() {
        return previousValue;
    }
    
}
