# Observable Properties #

A library to provide observable, versionable fields for Java objects.

## Usage ##

The basic usage is fairly simple. Instead of

```java
public class Pojo
{
    private String name;
    private int age;
    
    //getters and setters
}

Pojo pojo = new Pojo();
pojo.setName( "Mary" );
String name = pojo.getName();
```

You would have

```java
public class Pojo
{
    public final Prop<String> name = new Prop<String>;
    public final Prop<Integer> age = new Prop<Integer>;
}

Pojo pojo = new Pojo();
pojo.name.set( "Sue" );
String name = pojo.name.get();
```

## Features ##

What does this buy you? Several features that aren't (easily) available if 
you're using normal fields with getters and setters, such as:

1.  **Determine if a value has been set** - `pojo.name.set()` returns true if
    a value has been set for this property, and false if it has not. You no 
    longer need to rely on default or magic values.

1.  **Modified by** - `pojo.name.modifiedBy()` returns a String that can be 
    used as a user's unique identifier, fingering the individual responsible
    for making this change.

1.  **Modification timestamp** - `pojo.name.timestamp()` returns a long 
    representing the Unix time that this property was last modified.

1.  **Change listeners** - Each property can notify listeners of changes to 
    the property's value, using the `PropListener` interface and 
    `PropChangedEvent` class.

1.  **Version history** - (Optional) Each property can record a historical 
    record of all modifications made to it, including the value that was set,
    the user making the change, and the timestamp. This is represented by the
    `Version` class.