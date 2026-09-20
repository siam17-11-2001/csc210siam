public class Creature {

    String name;
    String size;
    int age;

    public Creature(String name, String size, int age) {
        this.name = name;
        this.size = size;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " is eating.");
    }

    public void talk() {
        System.out.println(name + " says hello!");
    }

    public void move() {
        System.out.println(name + " is moving.");
    }

    public void showInformation() {
        System.out.println("Name: " + name);
        System.out.println("Size: " + size);
        System.out.println("Age: " + age);
    }

    public static void main(String[] args) {

        Creature creature1 = new Creature("Leo", "Large", 5);

        creature1.showInformation();
        creature1.eat();
        creature1.talk();
        creature1.move();
    }
}