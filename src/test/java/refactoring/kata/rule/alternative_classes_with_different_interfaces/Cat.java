package refactoring.kata.rule.alternative_classes_with_different_interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author  <a href="mailto:meixuesong@gmail.com">Mei Xuesong</a>
 */
public class Cat {
    private String name;
    private List<String> friends = new ArrayList<>();

    public Cat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isCatFriendWith(String catName) {
        return friends.contains(catName);
    }

    public void addCatFriends(String catName) {
        friends.add(catName);
    }

    public List<String> getFriends() {
        return Collections.unmodifiableList(friends);
    }
}
