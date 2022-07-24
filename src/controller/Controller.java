package controller;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import GeoHash.GeoHashHelper;
import GeoHash.Location;
import JSON.requete;
import forme.Forme;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

//import views.View;

//import JSON.Requete;

public class Controller implements Initializable {


	@FXML
	private TextField textFieldRecherche;

	@FXML
	private DatePicker datePickerDateDebut;

	@FXML
	private DatePicker datePickerDateFin;

	@FXML
	private TextField textFieldDureeIntervalle;

	@FXML
	private TextField textFieldNombreIntervalle;

	@FXML
	private TextField textFieldGPS;

	@FXML
	private TextField textFieldGeohash;

	@FXML
	private RadioButton radioBtnPrecision3;

	@FXML
	private RadioButton radioBtnPrecision4;

	@FXML
	ToggleGroup precisionGeoHash;

	@FXML
	private TextArea textAreaInformations;

	@FXML
	private ListView<String> listViewChoixEspece;

	@FXML
	private Label labelRetourUtilisateur;

	@FXML
	private Label labelDate;

	@FXML
	private RadioButton radioBtnHistogramme;

	@FXML
	private Button btnDebut;

	@FXML
	private Button btnPause;

	@FXML
	private Button btnFin;

	@FXML
	private Pane Couleur1;

	@FXML
	private Pane Couleur2;

	@FXML
	private Pane Couleur3;

	@FXML
	private Pane Couleur4;

	@FXML
	private Pane Couleur5;

	@FXML
	private Pane Couleur6;

	@FXML
	private Pane Couleur7;

	@FXML
	private Pane Couleur8;

	@FXML
	private Label labelCouleur1;

	@FXML
	private Label labelCouleur2;

	@FXML
	private Label labelCouleur2Bis;

	@FXML
	private Label labelCouleur3;

	@FXML
	private Label labelCouleur3Bis;

	@FXML
	private Label labelCouleur4;

	@FXML
	private Label labelCouleur4Bis;

	@FXML
	private Label labelCouleur5;

	@FXML
	private Label labelCouleur5Bis;

	@FXML
	private Label labelCouleur6;

	@FXML
	private Label labelCouleur6Bis;

	@FXML
	private Label labelCouleur7;

	@FXML
	private Label labelCouleur7Bis;

	@FXML
	private Label labelCouleur8;

	@FXML
	private Pane pane3D;

	private AnimationTimer timer;

	private AnimationTimer timer1;

	@Override
	public void initialize(URL location, ResourceBundle resource) {

		Group root3D = new Group();

		// Load geometry
		ObjModelImporter objImporter = new ObjModelImporter();
		try {
			URL modeUrl = this.getClass().getResource("earth/earth.obj");
			objImporter.read(modeUrl);
		} catch (ImportException e) {
			// handle exception
			System.out.println(e.getMessage());
		}

		MeshView[] meshViews = objImporter.getImport();
		Group earth = new Group(meshViews);
		Group histo = new Group();
		root3D.getChildren().add(earth);
		root3D.getChildren().add(histo);

		// Add a camera group
		PerspectiveCamera camera = new PerspectiveCamera(true);
		new application.CameraManager(camera, pane3D, root3D);

		// Add point light
		PointLight light = new PointLight(Color.WHITE);
		light.setTranslateX(-180);
		light.setTranslateY(-90);
		light.setTranslateZ(-120);
		light.getScope().addAll(root3D);
		root3D.getChildren().add(light);

		// Add ambient light
		AmbientLight ambientLight = new AmbientLight(Color.WHITE);
		ambientLight.getScope().addAll(root3D);
		root3D.getChildren().add(ambientLight);

		SubScene subscene = new SubScene(root3D, 520.0, 530.0, true, SceneAntialiasing.BALANCED);
		subscene.setCamera(camera);
		subscene.setFill(Color.GREY);
		pane3D.getChildren().add(subscene);
		subscene.toBack();

		// Initialisation des param tres de certains boutons
		textAreaInformations.setEditable(false);
		//textFieldGeohash.setDisable(true);
		btnPause.setDisable(true);
		radioBtnPrecision3.setSelected(true);

		// --------------------------------Color-------------------------

		// Creation d'un tableau de couleur pour la legende
		ArrayList<PhongMaterial> couleurLegende = new ArrayList<PhongMaterial>();
		// couleurs de la legende
		final PhongMaterial vert1Material = new PhongMaterial();
		final PhongMaterial vert2Material = new PhongMaterial();
		final PhongMaterial vert3Material = new PhongMaterial();
		final PhongMaterial vert4Material = new PhongMaterial();
		final PhongMaterial vert5Material = new PhongMaterial();
		final PhongMaterial vert6Material = new PhongMaterial();
		final PhongMaterial vert7Material = new PhongMaterial();
		final PhongMaterial vert8Material = new PhongMaterial();

		// Hexadecimale :
		vert1Material.setDiffuseColor(new Color((float) 255 / 255, (float) 255 / 255, (float) 229 / 255, 0.8)); // #FFFFE5
		couleurLegende.add(vert1Material);
		
		vert2Material.setDiffuseColor(new Color((float) 247 / 255, (float) 252 / 255, (float) 185 / 255, 0.8)); // #F7FCB9
		couleurLegende.add(vert2Material);
		
		vert3Material.setDiffuseColor(new Color((float) 217 / 255, (float) 240 / 255, (float) 163 / 255, 0.8)); // #D9F0A3
		couleurLegende.add(vert3Material);
		
		vert4Material.setDiffuseColor(new Color((float) 173 / 255, (float) 221 / 255, (float) 142 / 255, 0.8)); // #ADDD8E
		couleurLegende.add(vert4Material);
		
		vert5Material.setDiffuseColor(new Color((float) 120 / 255, (float) 198 / 255, (float) 121 / 255, 0.8)); // #78C679
		couleurLegende.add(vert5Material);
		
		vert6Material.setDiffuseColor(new Color((float) 65 / 255, (float) 171 / 255, (float) 93 / 255, 0.8)); // #41AB5D
		couleurLegende.add(vert6Material);
		
		vert7Material.setDiffuseColor(new Color((float) 35 / 255, (float) 132 / 255, (float) 67 / 255, 0.8)); // #238443
		couleurLegende.add(vert7Material);
		
		vert8Material.setDiffuseColor(new Color((float) 0 / 255, (float) 90 / 255, (float) 50 / 255, 0.8)); // #005A32
		couleurLegende.add(vert8Material);

		RadioButton boutonSelect1 = (RadioButton) precisionGeoHash.getSelectedToggle();
		int[] legendeDelphinidae = requete.creationRequeteNomEspecePrecisionGeoHash(histo, couleurLegende,
				"delphinidae", Integer.parseInt(boutonSelect1.getText()), radioBtnHistogramme.isSelected());
		textFieldRecherche.setText("Delphinidae");
		setLegendeLabel(legendeDelphinidae[0], legendeDelphinidae[1]);
		


		// -------------------------------EVENT--------------------------------------------

		// Add event alt+click sur terre retourne geohash
		subscene.addEventHandler(MouseEvent.ANY, event -> {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {
				RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();

				// recuperation du geoHash
				setRetourUtilisateurValide("Récupération du géohash en cours.");
				labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
				PickResult pickResult = event.getPickResult();
				Point3D spaceCoord = pickResult.getIntersectedPoint();

				Point2D spaceCoord2D = Forme.SpaceCoordToGeoCoord(spaceCoord);
				Location loc = new Location("selectedGeoHash", spaceCoord2D.getX(), spaceCoord2D.getY());
				String geoHash = GeoHashHelper.getGeohash(loc, Integer.parseInt(boutonSelect.getText()));

				//maj listView et textArea
				setRetourUtilisateurValide("Espèces en cours de recherche pour le géohash " + geoHash + ".");
				labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
				majlistViewtextAreaFromGeohash(geoHash);

				// maj geoHash et cood GPS
				textFieldGeohash.setText(geoHash);
				BigDecimal t1 = new BigDecimal(spaceCoord2D.getX()).setScale(4, RoundingMode.HALF_UP);
				BigDecimal t2 = new BigDecimal(spaceCoord2D.getY()).setScale(4, RoundingMode.HALF_UP);
				String coordGPS = t1 + " ; " + t2;
				textFieldGPS.setText(coordGPS);

			}
		});

		// Add event quand on écrit dans textFieldRecherche affiche liste dans listeView
		textFieldRecherche.setOnKeyReleased(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				listViewChoixEspece.setDisable(false);
				textFieldRecherche.setStyle(null);
				labelRetourUtilisateur.setStyle(null);
				labelRetourUtilisateur.setText("Retour utilisateur :");
						
				// Si on écrit dans le textFieldRecherche et on appuie sur entrée => créé
				// requete adéquate
				if (event.getCode() == KeyCode.ENTER && textNonNull(textFieldRecherche.getText())) {
					// requete de recherche
					
					if(listViewChoixEspece.getItems().contains("Aucune espèce")) {
						
					}
					else {
						String nomEspeceSansEspace = textFieldRecherche.getText().replace(" ", "%20");
						demandeRequeteNomEspecePrecisionGeoHash(histo, couleurLegende, nomEspeceSansEspace);


						//MAJ de la textArea
						textAreaInformations.clear();
						ArrayList<StringBuilder> info =	requete.getInfoFromEspece(textFieldRecherche.getText().replace(" ", "%20"));
						textAreaInformations.setText(info.toString());
					}
					// Sinon on affiche les Especes commençant par les mêmes lettres dans la
					// ListView
				} else {
					ObservableList<String> itemsListView;
					itemsListView = FXCollections.observableArrayList(
							requete.autocompletionEspece(textFieldRecherche.getText().replace(" ", "%20")));
					listViewChoixEspece.setItems(itemsListView);
					

				}
				if(listViewChoixEspece.getItems().contains("Aucune espèce")) {
					listViewChoixEspece.setDisable(true);
					labelRetourUtilisateur.setText("Retour utilisateur : Aucune espèce ne commence par " + textFieldRecherche.getText());
					textFieldRecherche.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
					labelRetourUtilisateur.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
				}
			}
		});

		// Add event quand le radioBtnPrecision3 est sélectionné => créé requete avec
		// précision 3
		radioBtnPrecision3.setOnAction(event -> {
			String nomEspeceSansEspace = textFieldRecherche.getText().replace(" ", "%20");
			demandeRequeteNomEspecePrecisionGeoHash(histo, couleurLegende, nomEspeceSansEspace);
		});

		// Add event quand le radioBtnPrecision4 est sélectionné => créé requete avec
		// précision 4
		radioBtnPrecision4.setOnAction(event -> {
			String nomEspeceSansEspace = textFieldRecherche.getText().replace(" ", "%20");
			demandeRequeteNomEspecePrecisionGeoHash(histo, couleurLegende, nomEspeceSansEspace);
		});

		// Add event quand on écrit dans textFieldRecherche
		textFieldGPS.setOnKeyPressed(event -> {
			// Si on appuie sur la touche entr e
			if (event.getCode() == KeyCode.ENTER) {
				String gps = textFieldGPS.getText();
				gps = gps.replace(" ", "");
				gps = gps.replace("\n", "");
				String[] coordonnees = gps.split(";");

				RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();

				// Conversion coordonn es GPS en coordonn es GeoHash
				Point2D spaceCoord2D = new Point2D(Float.parseFloat(coordonnees[0]), Float.parseFloat(coordonnees[1]));
				Location loc = new Location("selectedGeoHash", spaceCoord2D.getX(), spaceCoord2D.getY());
				String geoHash = GeoHashHelper.getGeohash(loc, Integer.parseInt(boutonSelect.getText()));

				textFieldGeohash.setText(geoHash);
				
				//maj listView et textArea
				setRetourUtilisateurValide("Espèces en cours de recherche pour le géohash " + geoHash + ".");
				labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
				majlistViewtextAreaFromGeohash(geoHash);
			}
		});
		
		// Add event lorsque l'on écrit dans le géohash
		textFieldGeohash.setOnKeyPressed(event -> {
			// Si on appuie sur la touche entrée
			if (event.getCode() == KeyCode.ENTER) {
				String geoHash = textFieldGeohash.getText();
				String coordGPS = requete.getGPSFromGeohash(geoHash);
				textFieldGPS.setText(coordGPS);
				
				//maj listView et textArea
				setRetourUtilisateurValide("Espèces en cours de recherche pour le géohash " + geoHash + ".");
				labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
				majlistViewtextAreaFromGeohash(geoHash);
			}
		});

		// Add event quand on clique sur une espece de la ListView
		listViewChoixEspece.setOnMouseClicked(event -> {
			// Si on clique 1 fois sur une espece
			if (event.getClickCount() == 1) {
				//MAJ de la textArea
				textAreaInformations.clear();
				ArrayList<StringBuilder> info =	requete.getInfoFromEspece(listViewChoixEspece.getSelectionModel().getSelectedItem().replace(" ", "%20"));
				textAreaInformations.setText(info.toString());
			}
			// Si on clique 2 fois sur une espece
			if (event.getClickCount() == 2) {
				// modification du label de recherche
				textFieldRecherche.setText(listViewChoixEspece.getSelectionModel().getSelectedItem());

				// lancement de la recherche
				// lancement de la recherche
				String nomEspeceSansEspace = textFieldRecherche.getText().replace(" ", "%20");
				demandeRequeteNomEspecePrecisionGeoHash(histo, couleurLegende, nomEspeceSansEspace);
			}
		});

		// Add event quand radioBtnHistogramme est sélectionné ou déselectionné
		radioBtnHistogramme.setOnAction(event -> {
			if (textNonNull(textFieldRecherche.getText())) {

				/// lancement de la recherche
				String nomEspeceSansEspace = textFieldRecherche.getText().replace(" ", "%20");
				demandeRequeteNomEspecePrecisionGeoHash(histo, couleurLegende, nomEspeceSansEspace);
			}

		});

		// Add event quand btnDebut est sélectionné (Problème avec le btnPause)
		btnDebut.setOnAction(event -> {

			// On enlève tous les styles aux textField et datePicker
			datePickerDateDebut.setStyle(null);
			datePickerDateFin.setStyle(null);
			textFieldRecherche.setStyle(null);
			textFieldDureeIntervalle.setStyle(null);
			textFieldNombreIntervalle.setStyle(null);
			labelRetourUtilisateur.setStyle(null);

			radioBtnHistogramme.setDisable(true);

			// Test si on a date debut, date din, espèce recherchée et duree intervalle =>
			// créé requete
			if ((datePickerDateDebut.getValue() != null) && (datePickerDateFin.getValue() != null)
					&& (textFieldDureeIntervalle.getText().equals("") == false)
					&& (textFieldRecherche.getText().equals("") == false)) {
				btnPause.setDisable(false);
				textFieldDureeIntervalle
						.setText(String.valueOf((int) (Float.parseFloat(textFieldDureeIntervalle.getText()))));
				int nombreIntervalle = (datePickerDateFin.getValue().getYear()
						- datePickerDateDebut.getValue().getYear())
						/ Integer.parseInt(textFieldDureeIntervalle.getText());
				textFieldNombreIntervalle.setText(String.valueOf(nombreIntervalle));
				final long startNanoTime = System.nanoTime();

				// Création annimationTimer pour afficher les requetes différentes
				timer = new AnimationTimer() {

					LocalDate debut = datePickerDateDebut.getValue();

					
					@Override
					public void handle(long currentNanoTime) {
						labelRetourUtilisateur.setText("Retour Utilisateur : Animation entre le " + datePickerDateFin.getValue() + " et le "+ datePickerDateFin.getValue() +".");
						labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
						BigDecimal t = new BigDecimal((currentNanoTime - startNanoTime) / 1000000000.0).setScale(2,
								RoundingMode.HALF_UP);
						LocalDate fin = debut;
						System.out.println(t);
						// Add event quand btnPause est sélectionné
						btnPause.setOnAction(event -> {
							timer.stop();
							// Problème le timer ne se stoppe pas et tourne en fond
							btnPause.setText("Reprendre");
							radioBtnHistogramme.setDisable(false);
							// Add event quand btnPause est de nouveau sélectionné
							btnPause.setOnAction(event1 -> {
								timer.start();
								btnPause.setText("Pause");
								radioBtnHistogramme.setDisable(true);
							});
						});

						// Add event lorsque l'on sélectionne le btnFin => affiche requête espèce sans
						// prendre en compte le temps
						btnFin.setOnAction(event -> {
							btnPause.setDisable(true);
							timer.stop();
							RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
							int legende[] = requete.creationRequeteNomEspecePrecisionGeoHash(histo, couleurLegende,
									textFieldRecherche.getText().replace(" ", "%20"), Integer.parseInt(boutonSelect.getText()),
									radioBtnHistogramme.isSelected());
							setLegendeLabel(legende[0], legende[1]);
							radioBtnHistogramme.setDisable(false);
							labelDate.setText("Date");
							datePickerDateDebut.setValue(null);
							datePickerDateFin.setValue(null);
							textFieldDureeIntervalle.setText("");
							textFieldNombreIntervalle.setText("");
						});

						// On arrive à la fin de l'animation => affiche requête espèce sans prendre en
						// compte le temps
						if (t.doubleValue() > (nombreIntervalle * 5) - 0.04) {// - 0.04 permet d'arrêter avant la
																				// création d'une nouvelle requête
							timer.stop();
							RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
							int[] legende = requete.creationRequeteNomEspecePrecisionGeoHash(histo, couleurLegende,
									textFieldRecherche.getText().replace(" ", "%20"), Integer.parseInt(boutonSelect.getText()),
									radioBtnHistogramme.isSelected());
							setLegendeLabel(legende[0], legende[1]);
							radioBtnHistogramme.setDisable(false);
							labelDate.setText("Date");
						}
						// Synchronisation de l'horloge pour déclencher une nouvelle requête selon temps
						if ((t.intValue() % 5 == 0 && t.toString().contains("00"))
								|| (t.intValue() % 5 == 0 && t.toString().contains("01")) || t.doubleValue() == 0.01) {// Doublecmodulo 5 avec un décimal égal à 00 ou 01
							fin = debut.plusYears(Integer.parseInt(textFieldDureeIntervalle.getText()));
							RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
							int[] legende = requete.creationRequeteNomEspeceGeohashDebutFin(histo, couleurLegende,
									textFieldRecherche.getText().replace(" ", "%20"), Integer.parseInt(boutonSelect.getText()), debut, fin,
									radioBtnHistogramme.isSelected());
							setLegendeLabel(legende[0], legende[1]);
							// System.out.println("alllez");
							labelDate.setText(debut.toString() + " à " + fin.toString());
						}
						debut = fin;
					}

				};
				timer.start();

				// Test si on a date debut, espèce recherchée, nombre intervalle et duree
				// intervalle => créé requete
			} else if ((datePickerDateDebut.getValue() != null)
					&& (textFieldDureeIntervalle.getText().equals("") == false)
					&& (textFieldNombreIntervalle.getText().equals("") == false)
					&& (textFieldRecherche.getText().equals("") == false)) {
				btnPause.setDisable(false);
				// LocalDate dateFinCalcul = datePickerDateDebut.
				textFieldDureeIntervalle
						.setText(String.valueOf((int) (Float.parseFloat(textFieldDureeIntervalle.getText()))));
				textFieldNombreIntervalle
						.setText(String.valueOf((int) (Float.parseFloat(textFieldNombreIntervalle.getText()))));

				datePickerDateFin.setValue(
						datePickerDateDebut.getValue().plusYears(Integer.parseInt(textFieldNombreIntervalle.getText())
								* Integer.parseInt(textFieldDureeIntervalle.getText())));
				int nombreIntervalle = Integer.parseInt(textFieldNombreIntervalle.getText());
				final long startNanoTime = System.nanoTime();

				// Création annimationTimer pour afficher les requetes différentes
				timer1 = new AnimationTimer() {

					LocalDate debut = datePickerDateDebut.getValue();
					
					@Override
					public void handle(long currentNanoTime) {
						labelRetourUtilisateur.setText("Retour Utilisateur : Animation entre le " + datePickerDateFin.getValue() + " et le "+ datePickerDateFin.getValue() +".");
						labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
						BigDecimal t = new BigDecimal((currentNanoTime - startNanoTime) / 1000000000.0).setScale(2,
								RoundingMode.HALF_UP);
						LocalDate fin = debut;
						System.out.println(t);
						// Add event quand btnPause est sélectionné
						btnPause.setOnAction(event -> {
							// Problème le timer ne se stoppe pas et tourne en fond
							timer1.stop();
							btnPause.setText("Reprendre");
							radioBtnHistogramme.setDisable(false);
							// Add event quand btnPause est de nouveau sélectionné
							btnPause.setOnAction(event1 -> {
								timer1.start();
								btnPause.setText("Pause");
								radioBtnHistogramme.setDisable(true);
							});
						});

						// Add event lorsque l'on sélectionne le btnFin => affiche requête espèce sans
						// prendre en compte le temps
						btnFin.setOnAction(event -> {
							btnPause.setDisable(true);
							timer1.stop();
							RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
							int[] legende = requete.creationRequeteNomEspecePrecisionGeoHash(histo, couleurLegende,
									textFieldRecherche.getText().replace(" ", "%20"), Integer.parseInt(boutonSelect.getText()), true);
							radioBtnHistogramme.setDisable(false);
							setLegendeLabel(legende[0], legende[1]);
							radioBtnHistogramme.setDisable(false);
							
							labelDate.setText("Date");
							datePickerDateDebut.setValue(null);
							datePickerDateFin.setValue(null);
							textFieldDureeIntervalle.setText("");
							textFieldNombreIntervalle.setText("");
						});

						// On arrive à la fin de l'animation => affiche requête espèce sans prendre en
						// compte le temps
						if (t.doubleValue() > (nombreIntervalle * 5) - 0.04) { // - 0.04 permet d'arrêter avant la
																				// création d'une nouvelle requête
							timer1.stop();
							RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
							int[] legende = requete.creationRequeteNomEspecePrecisionGeoHash(histo, couleurLegende,
									textFieldRecherche.getText().replace(" ", "%20"), Integer.parseInt(boutonSelect.getText()), true);
							setLegendeLabel(legende[0], legende[1]);
							radioBtnHistogramme.setDisable(false);
							labelDate.setText("Date");
						}
						// Synchronisation de l'horloge pour déclencher une nouvelle requête selon temps
						if ((t.intValue() % 5 == 0 && t.toString().contains("00"))
								|| (t.intValue() % 5 == 0 && t.toString().contains("01")) || t.doubleValue() == 0.01) { // Double modulo 5 avec un décimal égal à 00 ou 01
																													
							fin = debut.plusYears(Integer.parseInt(textFieldDureeIntervalle.getText()));
							RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
							int[] legende = requete.creationRequeteNomEspeceGeohashDebutFin(histo, couleurLegende,
									textFieldRecherche.getText().replace(" ", "%20"), Integer.parseInt(boutonSelect.getText()), debut, fin,
									radioBtnHistogramme.isSelected());
							setLegendeLabel(legende[0], legende[1]);
							labelDate.setText(debut.toString() + " à " + fin.toString());
						}
						debut = fin;
					}
				};
				timer1.start();

				// Sinon on averti les problèmes à l'utilisateur
			} else {
				if (datePickerDateDebut.getValue() == null) {
					datePickerDateDebut.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
				}
				if (datePickerDateFin.getValue() == null) {
					datePickerDateFin.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
				}
				if (textFieldRecherche.getText() == "") {
					textFieldRecherche.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
				}
				if (textFieldNombreIntervalle.getText() == "") {
					textFieldNombreIntervalle.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
				}
				if (textFieldDureeIntervalle.getText() == "") {
					textFieldDureeIntervalle.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
				}
				labelRetourUtilisateur
						.setText("Retour Utilisateur : Les informations manquantes sont entourées en rouge.");
				labelRetourUtilisateur.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
			}
		});


	} 
	
	// -------------------END INITIALIZE--------------------------

	// ------------------- Quelques fonctions utililent ------------

	// Fonction vérifiant si le String est vide ou non
	public boolean textNonNull(String s) {
		if (s.isEmpty()) {
			return false;
		}
		return true;
	}
	
	// Fonction permettant de mettre à jour lors de l'écriture du GéoHash
	public void majlistViewtextAreaFromGeohash (String geoHash) {
		textAreaInformations.clear();

		// maj de la listView
		ObservableList<String> itemsListView;
		itemsListView = FXCollections.observableArrayList(requete.getEspeceFromGeohash(geoHash));
		listViewChoixEspece.setItems(itemsListView);

		// maj de la textArea
		ArrayList<StringBuilder> info = requete
				.getInfoFromEspeceGeoHash(textFieldRecherche.getText().replace(" ", "%20"), geoHash);
		textAreaInformations.setText(info.toString());
		
		// on informe l'utilisateur que la recherche est terminée
		setRetourUtilisateurValide("Recherche terminée pour le géohash " + geoHash + ".");
		labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
	}
	
	// Affiche dans le retour utilisateur puis recherche puis creationRequeteNomEspecePrecisionGeoHash
		public void demandeRequeteNomEspecePrecisionGeoHash(Group histo, ArrayList<PhongMaterial> couleurLegende, String nomEspeceSansEspace) {
			// R cup rer le bouton qui est s lectionn  parmi le ToggleGroup
			RadioButton boutonSelect = (RadioButton) precisionGeoHash.getSelectedToggle();
			if (radioBtnHistogramme.isSelected()) {
				setRetourUtilisateurValide("Requête validée => " + textFieldRecherche.getText() + " (précision:"
						+ boutonSelect.getText() + "+histogramme).");
			} else {
				setRetourUtilisateurValide("Requête validée => " + textFieldRecherche.getText() + " (précision:"
						+ boutonSelect.getText() + ").");
			}
			labelRetourUtilisateur.setStyle("-fx-border-color: green ; -fx-border-width: 1px ;");
			int[] legende = requete.creationRequeteNomEspecePrecisionGeoHash(histo, couleurLegende,
					nomEspeceSansEspace, Integer.parseInt(boutonSelect.getText()),
					radioBtnHistogramme.isSelected());
			setLegendeLabel(legende[0], legende[1]);
		}

	// Initialise les retours utilisateurs
	public void setRetourUtilisateurValide(String retour) {
		labelRetourUtilisateur.setText("Retour Utilisateur : " + retour);
	}

	// Initialise les labels de legende
	public void setLegendeLabel(int Debut, int pas) {
		labelCouleur1.setText("< " + String.valueOf(Debut));
		labelCouleur2.setText(String.valueOf(Debut + pas * 0));
		labelCouleur2Bis.setText(String.valueOf(Debut + pas * 1));
		labelCouleur3.setText(String.valueOf(Debut + pas * 1));
		labelCouleur3Bis.setText(String.valueOf(Debut + pas * 2));
		labelCouleur4.setText(String.valueOf(Debut + pas * 2));
		labelCouleur4Bis.setText(String.valueOf(Debut + pas * 3));
		labelCouleur5.setText(String.valueOf(Debut + pas * 3));
		labelCouleur5Bis.setText(String.valueOf(Debut + pas * 4));
		labelCouleur6.setText(String.valueOf(Debut + pas * 4));
		labelCouleur6Bis.setText(String.valueOf(Debut + pas * 5));
		labelCouleur7.setText(String.valueOf(Debut + pas * 5));
		labelCouleur7Bis.setText(String.valueOf(Debut + pas * 6));
		labelCouleur8.setText("> " + String.valueOf(Debut + pas * 6));
	}

}
