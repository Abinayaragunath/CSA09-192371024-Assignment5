# A4 – Java Serialization and Object Graphs

## 1. Problem Description

Consider a hostel management system containing the following object relationships:

- Hostel contains Room objects.
- Room contains Resident objects.
- Resident stores personal details such as name and register number.

Java serialization can save this connected object structure into a file.

## 2. What is Serialization?

Serialization is the process of converting a Java object into a byte stream so that it can be stored in a file or transferred.

Deserialization converts the stored byte stream back into Java objects.

The Serializable interface marks a class as eligible for default Java serialization.

## 3. Object Graph

Example relationship:

```text
Hostel
  |
  +---- Room 101
  |       |
  |       +---- Resident A
  |       +---- Resident B
  |
  +---- Room 102
          |
          +---- Resident C
```

When the Hostel object is serialized, its reachable serializable Room and Resident objects are also serialized.

## 4. Example Classes

```java
import java.io.Serializable;
import java.util.List;

class Hostel implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Room> rooms;
}

class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    int roomNumber;
    List<Resident> residents;
}

class Resident implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    String registerNumber;

    transient String password;
}
```

The classes implement Serializable so their object data can be serialized.

## 5. Use of transient

The transient keyword prevents a field from being included in default Java serialization.

| Field | Decision | Reason |
|---|---|---|
| Name | Serialize | Required resident information |
| Register number | Serialize | Required identification |
| Password | transient | Avoid storing sensitive credentials |
| Database Connection | transient | Connection is not a serializable persistent object |

A live database Connection should be re-established after deserialization. Passwords should be stored using secure password hashing rather than plain text.

## 6. Object Graph Serialization

Example:

```java
ObjectOutputStream out =
    new ObjectOutputStream(
        new FileOutputStream("hostel.dat"));

out.writeObject(hostel);
out.close();
```

The complete reachable serializable object graph is written to the file.

To restore it:

```java
ObjectInputStream in =
    new ObjectInputStream(
        new FileInputStream("hostel.dat"));

Hostel hostel =
    (Hostel) in.readObject();

in.close();
```

## 7. serialVersionUID and Versioning

serialVersionUID identifies the version of a serializable class.

When the class structure changes, the serialVersionUID helps Java determine whether an older serialized object is compatible with the updated class.

If a new phone field is added:

```java
String phoneNumber;
```

With the same serialVersionUID, an older serialized object can generally be read, and the newly added field receives its default value, which is null for String.

If incompatible changes are made, deserialization may fail. Changing serialVersionUID does not automatically migrate old data.

## 8. Serialization vs CSV/JSON

| Feature | Java Serialization | CSV/JSON |
|---|---|---|
| Format | Binary | Text |
| Human readable | No | Yes |
| Java object support | Direct object graph | Requires mapping |
| Cross-language support | Limited | Broad |
| Compatibility | Sensitive to class changes | Easier to design for versioning |
| File size | Often compact, but varies | Often larger, but varies |

## 9. Conclusion

Java serialization is useful for saving connected Java object graphs. The transient keyword excludes fields that should not be serialized, while serialVersionUID supports compatibility checks between class versions. CSV and JSON are better choices when human readability and cross-language compatibility are important.