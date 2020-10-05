package refactoring.kata.rule.alternative_classes_with_different_interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author  <a href="mailto:meixuesong@gmail.com">Mei Xuesong</a>
 */
public class Dog {
    private String name;
    private List<String> friends = new ArrayList<>();

    public Dog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isDogFriendWith(String dogName) {
        return friends.contains(dogName);
    }

    public void addDogFriends(String dogName) {
        if (!isDogFriendWith(dogName)) {
            friends.add(dogName);
        }
    }

    public List<String> getFriends() {
        return Collections.unmodifiableList(friends);
    }
}
