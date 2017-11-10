package ua.dgurin.workhackbot;

/**
 * @author Danylo_Hurin.
 */
public class WordDefinition {

    private String word;
    private String definition;
    private String translation;
    private String sample;
    private String other;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "WordDefinition{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                ", translation='" + translation + '\'' +
                ", sample='" + sample + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
