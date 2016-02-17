package props;

public class Pojo
{
    public final Prop<String> name;
    public final Prop<Integer> age;
    
    public Pojo(){
        name = new Prop( this, null, true );
        age = new Prop( this, null, true );
    }
    
    public Pojo( String name, int age ){
        this();
        this.name.set( name );
        this.age.set( age );
    }
}
