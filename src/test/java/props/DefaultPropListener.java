package props;

public class DefaultPropListener implements PropListener
{
    private int count;
    private Object current;
    private Object previous;
    
    public void propertyChanged( PropChangedEvent event ) {
        count++;
        
        current = event.value();
        previous = event.previousValue();
    }
    
    public int count(){
        return count;
    }
    
    public Object current(){
        return current;
    }
    
    public Object previous(){
        return previous;
    }
    
    public void reset(){
        count = 0;
        current = null;
        previous = null;
    }
    
}
