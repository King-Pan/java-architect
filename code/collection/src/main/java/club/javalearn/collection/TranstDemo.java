package club.javalearn.collection;

/**
 * @author king-pan
 * @date 2019/5/6
 * @Description ${DESCRIPTION}
 */
public class TranstDemo {


    public static void main(String[] args) {
        int age = 20;
        setAge(age);
        System.out.println(age);

        Person person = new Person();
        person.name = "king";
        person.age = 28;
        setPerson(person);
        System.out.println(person);

        String name = "tomcat";
        setName(name);
        System.out.println(name);

    }


    private static void setAge(int age){
        age = 30;
    }

    private static void setName(String name){
        name = "pan";
    }

    private static void setPerson(Person person){
        person.age = 100;
        person.name = "Hello Kit";
    }





    static class Person{
        String name;
        int age;

        @Override
        public String toString() {
            return name + "," + age;
        }
    }
}
