package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Theme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    JFXComboBox<String> themeSelect;

    @FXML
    HBox themeBox;

    @FXML
    private JFXComboBox<String> langBox;

    @FXML
    private JFXTextField hostName;

    @FXML
    private JFXTextField databaseName;

    @FXML
    private JFXButton linkButton;

    @FXML
    private JFXButton embeddedButton;

    @FXML
    private Label connectrDBLabel;

    @FXML
    private Label hostnameLabel;

    @FXML
    private Label dbLabel;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        linkButton.setOnAction(event -> linkServerDatabase());
        embeddedButton.setOnAction(event -> switchToEmbedded());
        fillLangBox();
        int selection = 0;
        System.out.println(App.selectedLocale.name());
        switch (App.selectedLocale.name()) {
            case "en_US":
                selection = 0;
                break;
            case "es_ES":
                selection = 1;
                break;
            case "ja_JP":
                selection = 2;
                break;
            case "ch_CN":
                selection = 3;
                break;
            case "ru_RU":
                selection = 4;
                break;
        }
        langBox.getSelectionModel().select(selection);

        if (Session.getAccount() == null || !Session.getAccount().isAdmin()) {
            linkButton.setVisible(false);
            linkButton.setManaged(false);
            embeddedButton.setVisible(false);
            embeddedButton.setManaged(false);
            databaseName.setVisible(false);
            databaseName.setManaged(false);
            hostName.setVisible(false);
            hostName.setManaged(false);
            connectrDBLabel.setVisible(false);
            connectrDBLabel.setManaged(false);
            hostnameLabel.setVisible(false);
            hostnameLabel.setManaged(false);
            dbLabel.setVisible(false);
            dbLabel.setManaged(false);

        }

        themeSelect = new JFXComboBox<String>();
        themeBox.getChildren().add(themeSelect);

        themeSelect.getItems().add("Theme 1");
        themeSelect.getItems().add("Theme 2");
        themeSelect.getItems().add("Night");
        themeSelect.getItems().add("Holiday");
        themeSelect.getItems().add("Wong");

        int tSelection = 0;
        switch (Session.getAccount().getTheme().name()) {
            case "BLUE_SKY":
                tSelection = 0;
                break;
            case "CLOUDS":
                tSelection = 1;
                break;
            case "DARK":
                tSelection = 2;
                break;
            case "HOLIDAY":
                tSelection = 3;
                break;
            case "WONG":
                tSelection = 4;
                break;
        }

        themeSelect.getSelectionModel().select(tSelection);
    }

    private void fillLangBox() {
        langBox.getItems().add(App.resourceBundle.getString("key.english"));
        langBox.getItems().add(App.resourceBundle.getString("key.spanish"));
        langBox.getItems().add(App.resourceBundle.getString("key.japanese"));
        langBox.getItems().add(App.resourceBundle.getString("key.chinese"));
        langBox.getItems().add(App.resourceBundle.getString("key.russian"));
        langBox.getSelectionModel().selectFirst();
    }

    private void linkServerDatabase(){
        String host = hostName.getText();
        String database = databaseName.getText();
        try{
            Database.setRemoteDB(host, database);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        App.switchPage(Pages.MAIN);
    }

    private void switchToEmbedded(){
        try{
            Database.setEmbeddedDB();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        App.switchPage(Pages.MAIN);
    }


    @FXML
    private void handleConfirm(ActionEvent e) {
        try {
            switch (themeSelect.getValue()) {
                case "Theme 1":
                    Session.getAccount().setTheme(Theme.BLUE_SKY);
                    break;
                case "Theme 2":
                    Session.getAccount().setTheme(Theme.CLOUDS);
                    break;
                case "Holiday":
                    Session.getAccount().setTheme(Theme.HOLIDAY);
                    break;
                case "Night":
                    Session.getAccount().setTheme(Theme.DARK);
                    break;
                case "Wong":
                    Session.getAccount().setTheme(Theme.WONG);
                    break;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(langBox.getValue().equals(App.resourceBundle.getString("key.english"))) {
            App.switchLocale("en", "US", LocaleType.en_US);
        }
        else if (langBox.getValue().equals(App.resourceBundle.getString("key.spanish"))) {
            App.switchLocale("es", "ES", LocaleType.es_ES);
        }

        else if (langBox.getValue().equals(App.resourceBundle.getString("key.japanese"))) {
            App.switchLocale("ja", "JP", LocaleType.ja_JP);
        }

        else if (langBox.getValue().equals(App.resourceBundle.getString("key.chinese"))) {
            App.switchLocale("zh", "CN", LocaleType.zh_CN);
        }

        else if (langBox.getValue().equals(App.resourceBundle.getString("key.russian"))) {
            App.switchLocale("ru", "RU", LocaleType.ru_RU);
        }

        App.switchPage(Pages.MAIN);

    }

}
