package org.example.view;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.stream.Collectors;
import javafx.stage.Stage;
import org.example.factory.DatabaseFactory;
import org.example.model.Recept;
import org.example.dao.ReceptDao;

import java.util.List;

/**
 * Hlavné okno aplikácie Kuchárka
 * Obsahuje:
 * - Filtre (kategória, názov)
 * - Tabuľka receptov
 * - Náhľad receptu
 */
public class MainWindow extends Application {

    private DatabaseFactory databaseFactory;
    private ReceptDao receptDao;
    private ObservableList<Recept> receptyList = FXCollections.observableArrayList();
    private TableView<Recept> tableView;
    private TextArea náhľadTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializácia databázy
            databaseFactory = DatabaseFactory.initializeWithDefaults();
            receptDao = databaseFactory.getReceptDao();
            
            // Načítanie receptov
            loadRecepty();
            
            // Vytvorenie UI
            primaryStage.setTitle("Kuchárka - Recepty");
            primaryStage.setScene(createMainScene(primaryStage));
            primaryStage.setMaximized(true);
            primaryStage.show();
            
        } catch (Exception e) {
            showErrorDialog("Chyba pri spustení aplikácie", 
                           "Nepodarilo sa pripojiť k databáze: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vytvorí hlavnú scénu
     */
    private Scene createMainScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Horný panel - Nadpis
        root.setTop(createHeader());
        
        // Ďalší obsah
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setDividerPositions(0.6);
        
        // Horná časť - Filtre a tabuľka
        VBox topContent = new VBox(10);
        topContent.setPadding(new Insets(10));
        topContent.getChildren().addAll(
            createFilterPanel(),
            createTableView()
        );
        
        // Dolná časť - Náhľad
        VBox bottomContent = new VBox(10);
        bottomContent.setPadding(new Insets(10));
        bottomContent.getChildren().addAll(
            new Label("Náhľad receptu:"),
            createNáhľadPanel()
        );
        
        splitPane.getItems().addAll(topContent, bottomContent);
        root.setCenter(splitPane);
        
        return new Scene(root, 1200, 800);
    }

    /**
     * Vytvorí header s názvom aplikácie
     */
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 0, 10, 0));
        
        Label title = new Label("Kuchárka");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitle = new Label("Recepty, jedálniček a nákupný zoznam");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        header.getChildren().addAll(title, subtitle);
        
        return header;
    }

    /**
     * Vytvorí panel s filtermi
     */
    private HBox createFilterPanel() {
        HBox filterPanel = new HBox(15);
        filterPanel.setAlignment(Pos.CENTER_LEFT);
        
        // Filter podľa kategórie
        Label kategoriaLabel = new Label("Kategória:");
        ComboBox<String> kategoriaCombo = new ComboBox<>();
        kategoriaCombo.getItems().addAll("Všetky", "Polievka", "Hlavné jedlo", "Priloha", "Dezert", "Salát", "Snihdane");
        kategoriaCombo.getSelectionModel().selectFirst();
        kategoriaCombo.setPrefWidth(150);
        
        // Filter podľa názvu
        Label nazovLabel = new Label("Názov:");
        TextField nazovField = new TextField();
        nazovField.setPromptText("Zadajte názov receptu...");
        nazovField.setPrefWidth(250);
        
        // Tlačidlo na vyhľadanie
        Button searchButton = new Button("Vyhľadaj");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        searchButton.setOnAction(e -> {
            String selectedKategoria = kategoriaCombo.getSelectionModel().getSelectedItem();
            String nazov = nazovField.getText().trim();
            filterRecepty(selectedKategoria, nazov);
        });
        
        // Tlačidlo na obnovenie
        Button refreshButton = new Button("Obnov");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        refreshButton.setOnAction(e -> loadRecepty());
        
        filterPanel.getChildren().addAll(
            kategoriaLabel, kategoriaCombo,
            nazovLabel, nazovField,
            searchButton,
            refreshButton
        );
        
        return filterPanel;
    }

    /**
     * Vytvorí tabuľku receptov
     */
    private TableView<Recept> createTableView() {
        tableView = new TableView<>();
        tableView.setItems(receptyList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Stĺpce
        TableColumn<Recept, String> nazovCol = new TableColumn<>("Názov");
        nazovCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNazov()));
        nazovCol.setPrefWidth(250);
        
        TableColumn<Recept, String> kategoriaCol = new TableColumn<>("Kategória");
        kategoriaCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getKategoria() != null) {
                return new SimpleStringProperty(cellData.getValue().getKategoria().getNazov());
            }
            return new SimpleStringProperty("");
        });
        kategoriaCol.setPrefWidth(150);
        
        TableColumn<Recept, Number> casCol = new TableColumn<>("Čas (min)");
        casCol.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getCasPripravyMin()));
        casCol.setPrefWidth(100);
        
        TableColumn<Recept, Number> porcieCol = new TableColumn<>("Porcie");
        porcieCol.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getPocetPorcii()));
        porcieCol.setPrefWidth(80);
        
        tableView.getColumns().addAll(nazovCol, kategoriaCol, casCol, porcieCol);
        
        // Výber riadku - zobrazenie náhľadu
        tableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    showNáhľad(newValue);
                }
            }
        );
        
        return tableView;
    }

    /**
     * Vytvorí panel pre náhľad receptu
     */
    private VBox createNáhľadPanel() {
        VBox náhľadPanel = new VBox(10);
        náhľadPanel.setPadding(new Insets(10));
        náhľadPanel.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1px;");
        
        // TextArea pre postup
        náhľadTextArea = new TextArea();
        náhľadTextArea.setEditable(false);
        náhľadTextArea.setWrapText(true);
        náhľadTextArea.setPrefRowCount(10);
        náhľadTextArea.setStyle("-fx-font-family: monospace;");
        
        // Info panel (názov, čas, porcie)
        HBox infoPanel = new HBox(20);
        infoPanel.setAlignment(Pos.CENTER_LEFT);
        
        Label receptNazov = new Label();
        receptNazov.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        receptNazov.setMinWidth(300);
        
        Label receptCas = new Label();
        receptCas.setStyle("-fx-font-size: 14px;");
        
        Label receptPorcie = new Label();
        receptPorcie.setStyle("-fx-font-size: 14px;");
        
        infoPanel.getChildren().addAll(receptNazov, receptCas, receptPorcie);
        
        náhľadPanel.getChildren().addAll(infoPanel, náhľadTextArea);
        
        return náhľadPanel;
    }

    /**
     * Načítanie receptov z databázy
     */
    private void loadRecepty() {
        try {
            List<Recept> recepty = receptDao.findAll();
            receptyList.setAll(recepty);
        } catch (Exception e) {
            showErrorDialog("Chyba", "Nepodarilo sa načítať recepty: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Filter receptov podľa kategórie a názvu
     */
    private void filterRecepty(String kategoria, String nazov) {
        try {
            List<Recept> recepty;
            
            if ("Všetky".equals(kategoria) && (nazov == null || nazov.isEmpty())) {
                recepty = receptDao.findAll();
            } else if ("Všetky".equals(kategoria)) {
                recepty = receptDao.findByNazovContaining(nazov);
            } else {
                // Kombinácia kategórie a názvu
                recepty = receptDao.findAll().stream()
                    .filter(r -> kategoria.equals(r.getKategoria() != null ? r.getKategoria().getNazov() : ""))
                    .filter(r -> nazov == null || nazov.isEmpty() || 
                           r.getNazov().toLowerCase().contains(nazov.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            receptyList.setAll(recepty);
        } catch (Exception e) {
            showErrorDialog("Chyba", "Nepodarilo sa filtrovať recepty: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Zobrazenie náhľadu receptu
     */
    private void showNáhľad(Recept recept) {
        // Aktualizovať info panel
        // Získanie referencie na Label v infoPanel - zjednodušenie
        // V praxi by sme použili FX elementy priamo
        
        StringBuilder náhľad = new StringBuilder();
        náhľad.append("Recept: ").append(recept.getNazov()).append("\n");
        náhľad.append("========================================\n");
        náhľad.append("Kategória: ").append(recept.getKategoria() != null ? recept.getKategoria().getNazov() : "Neznáma").append("\n");
        náhľad.append("Čas prípravy: ").append(recept.getCasPripravyMin() != null ? recept.getCasPripravyMin() : "?").append(" minút\n");
        náhľad.append("Počet porcií: ").append(recept.getPocetPorcii()).append("\n");
        náhľad.append("\nPostup:\n");
        náhľad.append("----------------------------------------\n");
        náhľad.append(recept.getPostup() != null ? recept.getPostup() : "Postup nie je zadaný");
        
        náhľadTextArea.setText(náhľad.toString());
    }

    /**
     * Zobrazenie error dialóg
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        // Zatvorenie databázy
        DatabaseFactory.close();
    }
}
