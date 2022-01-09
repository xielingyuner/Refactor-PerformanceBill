package refactoring.performancebill.domain.model.play;

public class Play {
    String id;
    String name;
    String type;

    public Play(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
