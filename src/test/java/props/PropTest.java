package props;

import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class PropTest
{
    @Test
    public void testGettersAndSetters() throws Exception{
        Pojo thomas = new Pojo();
        Assert.assertFalse( "Property should not be set", thomas.name.set() );
        Assert.assertFalse( "Property should not be set", thomas.age.set() );
        
        Assert.assertEquals( "Timestamp should not be set", -1, thomas.name.timestamp() );
        Assert.assertEquals( "Timestamp should not be set", -1, thomas.age.timestamp() );
        
        thomas.name.set( "Thomas" );
        thomas.age.set( 36 );
        
        Assert.assertTrue( "Property should be set", thomas.name.set() );
        Assert.assertTrue( "Property should be set", thomas.age.set() );
        
        Assert.assertNotEquals( "Timestamp should be set", -1, thomas.name.timestamp() );
        Assert.assertNotEquals( "Timestamp should be set", -1, thomas.age.timestamp() );
        
        Pojo aj = new Pojo( "AJ", 32 );
        
        Assert.assertTrue( "Property should be set", aj.name.set() );
        Assert.assertTrue( "Property should be set", aj.age.set() );
        
        Assert.assertNotEquals( "Timestamp should be set", -1, aj.name.timestamp() );
        Assert.assertNotEquals( "Timestamp should be set", -1, aj.age.timestamp() );
        
        String testName = thomas.name.get();
        Assert.assertEquals( "Getter returned unexpected value", "Thomas", testName );
        
        testName = aj.name.get();
        Assert.assertEquals( "Getter returned unexpected value", "AJ", testName );
        
        int testAge = thomas.age.get();
        Assert.assertEquals( "Getter returned unexpected value", 36, testAge );
        
        testAge = aj.age.get();
        Assert.assertEquals( "Getter returned unexpected value", 32, testAge );
    }
    
    @Test
    public void testListeners() throws Exception {
        DefaultPropListener name = new DefaultPropListener();
        DefaultPropListener age = new DefaultPropListener();
        
        Pojo thomas = new Pojo( "Thomas", 36 );
        thomas.name.addListener( name );
        thomas.age.addListener( age );
        
        thomas.name.set( "Grand Lord Hellbringer" );
        Assert.assertEquals( "Wrong notification count", 1, name.count() );
        Assert.assertEquals( "Wrong notification count", "Thomas", name.previous() );
        Assert.assertEquals( "Wrong notification count", "Grand Lord Hellbringer", name.current() );
        
        thomas.name.set( "Master of the Creeping Darkness" );
        Assert.assertEquals( "Wrong notification count", 2, name.count() );
        Assert.assertEquals( "Wrong notification count", "Grand Lord Hellbringer", name.previous() );
        Assert.assertEquals( "Wrong notification count", "Master of the Creeping Darkness", name.current() );
        
        thomas.name.removeListener( name );
        thomas.name.set( "You should not see a notification in the listener" );
        Assert.assertEquals( "Wrong notification count", 2, name.count() );
        Assert.assertEquals( "Wrong notification count", "Grand Lord Hellbringer", name.previous() );
        Assert.assertEquals( "Wrong notification count", "Master of the Creeping Darkness", name.current() );
        
        thomas.age.set( 437 );
        Assert.assertEquals( "Wrong notification count", 1, age.count() );
        Assert.assertEquals( "Wrong notification count", 36, age.previous() );
        Assert.assertEquals( "Wrong notification count", 437, age.current() );
        
        thomas.age.set( 12 );
        Assert.assertEquals( "Wrong notification count", 2, age.count() );
        Assert.assertEquals( "Wrong notification count", 437, age.previous() );
        Assert.assertEquals( "Wrong notification count", 12, age.current() );
        
        thomas.age.removeListeners();
        thomas.age.set( 632 );
        Assert.assertEquals( "Wrong notification count", 2, age.count() );
        Assert.assertEquals( "Wrong notification count", 437, age.previous() );
        Assert.assertEquals( "Wrong notification count", 12, age.current() );
    }
    
    @Test
    public void testVersioning() throws Exception {
        Pojo thomas = new Pojo( "Thomas", 36 );
        
        thomas.name.set( "Grand Lord Hellbringer" );
        thomas.name.set( "Master of the Creeping Darkness" );
        thomas.name.set( "Fell Marshall of the Undying Hordes" );
        
        List<Version<String>> names = thomas.name.versions();
        Assert.assertNotNull( "Versions were null", names );
        Assert.assertEquals( "Wrong version count", 3, names.size() );
        
        Assert.assertEquals( "Wrong version value", "Thomas", names.get(0).value() );
        Assert.assertEquals( "Wrong version value", "Grand Lord Hellbringer", names.get(1).value() );
        Assert.assertEquals( "Wrong version value", "Master of the Creeping Darkness", names.get(2).value() );
        
        thomas.age.set( 437 );
        thomas.age.set( 12 );
        thomas.age.set( -1 );
        
        List<Version<Integer>> ages = thomas.age.versions();
        
        Assert.assertNotNull( "Versions were null", ages );
        Assert.assertEquals( "Wrong version count", 3, ages.size() );
        
        Assert.assertEquals( "Wrong version value", new Integer(36), ages.get(0).value() );
        Assert.assertEquals( "Wrong version value", new Integer(437), ages.get(1).value() );
        Assert.assertEquals( "Wrong version value", new Integer(12), ages.get(2).value() );
    }
    
    @Test
    public void testEnhanedVersioning() throws Exception {
        Pojo thomas = new Pojo( "Thomas", 36 );

        String user1 = UUID.randomUUID().toString();
        long date1 = new DateTime( 2016,1,1,0,0,0 ).getMillis();
        thomas.name.set( "Grand Lord Hellbringer", user1, date1 );
        
        String user2 = UUID.randomUUID().toString();
        long date2 = new DateTime( 2016,1,1,0,0,0 ).getMillis();
        thomas.name.set( "Master of the Creeping Darkness", user2, date2 );
        
        String user3 = UUID.randomUUID().toString();
        long date3 = new DateTime( 2016,1,1,0,0,0 ).getMillis();
        thomas.name.set( "Fell Marshall of the Undying Hordes", user3, date3 );
        
        List<Version<String>> names = thomas.name.versions();
        Assert.assertNotNull( "Versions were null", names );
        Assert.assertEquals( "Wrong version count", 3, names.size() );
        
        Assert.assertEquals( "Wrong version value", "Thomas", names.get(0).value() );
        Assert.assertEquals( "Wrong version value", "Grand Lord Hellbringer", names.get(1).value() );
        Assert.assertEquals( "Wrong version value", "Master of the Creeping Darkness", names.get(2).value() );
        
        Assert.assertEquals( "Wrong user id", null, names.get(0).modifiedBy());
        Assert.assertEquals( "Wrong user id", user1, names.get(1).modifiedBy() );
        Assert.assertEquals( "Wrong user id", user2, names.get(2).modifiedBy() );

        //we don't know what the timestamp for the first version (created by 
        //the constructor)
        Assert.assertEquals( "Wrong timestamp", date1, names.get(1).timestamp() );
        Assert.assertEquals( "Wrong timestamp", date2, names.get(2).timestamp() );
    }
}
