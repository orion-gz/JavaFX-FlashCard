package orion_gz.github.io.flashcard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Data {
    private static final String SETS_FILE = "flashcardSets.dat";

    public static ObservableList<FlashCardSet> readFlashCards() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SETS_FILE))) {
            ObservableList<FlashCardSet> loadedSets = FXCollections.observableArrayList();
            while (true) {
                FlashCardSet flashcardSet = new FlashCardSet("");
                try {
                    flashcardSet.deserialize(ois);
                    loadedSets.add(flashcardSet);
                } catch (EOFException e) {
                    break; // 파일의 끝에 도달하면 루프 종료
                }
            }
            return loadedSets;
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    public static void saveFlashCards(ObservableList<FlashCardSet> flashCardSets) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SETS_FILE))) {
            for (FlashCardSet flashCardSet : flashCardSets) {
                flashCardSet.serialize(oos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class FlashCardSet implements Serializable {
        private String setName;
        private ObservableList<FlashCard> cards;

        public FlashCardSet(String setName) {
            this.setName = setName;
            this.cards = FXCollections.observableArrayList();
        }

        public String getSetName() {
            return setName;
        }

        public boolean isEmpty() { return this.cards.isEmpty(); }

        public ObservableList<FlashCard> getCards() {
            return cards;
        }

        public void addCard(FlashCard card) {
            cards.add(card);
        }

        public void removeCard(FlashCard card) {
            cards.remove(card);
        }

        @Override
        public String toString() {
            return setName;
        }

        public void serialize(ObjectOutputStream oos) throws IOException {
            oos.writeObject(setName);
            oos.writeObject(new ArrayList<>(cards)); // ObservableList를 일반 List로 변환하여 저장
        }

        public void deserialize(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            setName = (String) ois.readObject();
            List<FlashCard> cardList = (List<FlashCard>) ois.readObject();
            cards.setAll(cardList); // 저장된 List를 ObservableList로 변환하여 복원
        }
    }

    public static class FlashCard implements Serializable {

        private String word;
        private String definition;
        private String hint;
        private String memo;

        public FlashCard(String word, String definition, String hint, String memo) {
            this.word = word;
            this.definition = definition;
            this.hint = hint;
            this.memo = memo;
        }

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }

        public String getHint() {
            return hint;
        }

        public String getMemo() {
            return memo;
        }

        public void setWord(String word) { this.word = word; }

        public void setDefinition(String definition) { this.definition = definition; }

        public void setHint(String hint) { this.hint = hint; }

        public void setMemo(String memo) { this.memo = memo; }

        @Override
        public String toString() {
            return word;
        }

        public void serialize(ObjectOutputStream oos) throws IOException {
            oos.writeObject(word);
            oos.writeObject(definition);
            oos.writeObject(hint);
            oos.writeObject(memo);
        }

        public void deserialize(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            word = (String) ois.readObject();
            definition = (String) ois.readObject();
            hint = (String) ois.readObject();
            memo = (String) ois.readObject();
        }
    }
}