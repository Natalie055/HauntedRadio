package Hauntedradio;

public class GhostChannel extends Channel {
    public GhostChannel(int number) {
        super(number, "Ghost Signal");
    }
    @Overridepublic void broadcast() {
        System.our.println("A ghostly voice emerge: " + "WELCOME TO THE HIDDEN FREQUENCY...'\n" + "help")
    }
}