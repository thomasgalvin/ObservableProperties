package props;

public class Pojo
{
    public final Prop<String> name;
    public final Prop<Integer> age;
    
    public Pojo(){
        this( true );
    }
    
    public Pojo( boolean recordChanges ){
        name = new Prop( this, null, recordChanges );
        age = new Prop( this, null, recordChanges );
    }
    
    public Pojo( String name, int age ){
        this( true );
        this.name.set( name );
        this.age.set( age );
    }
    
    public Pojo( String name, int age, boolean recordChanges ){
        this( recordChanges );
        this.name.set( name );
        this.age.set( age );
    }
}
