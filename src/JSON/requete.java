package JSON;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import forme.Forme;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class requete {

	// Permet de créer un JSONObject à partir d'un URL
	public static JSONObject readJsonFromUrl(String url) {
		String json = "";

		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20)).build();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json").GET().build();

		try {
			json = client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).get(10,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JSONObject(json);
	}

	// Même fonction que readJsonFromUrl mais retourne un JSONArray
	public static JSONArray readJsonArrayFromUrl(String url) {

		String json = "";

		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20)).build();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json").GET().build();

		try {
			json = client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).get(10,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONArray(json);
	}

	// Permet de faire une requete à partir nom espece + precision GeoHash
	public static int[] creationRequeteNomEspecePrecisionGeoHash(Group parent, ArrayList<PhongMaterial> couleur,
			String Espece, int precisionGeohash, boolean Histo) {
		String url = "https://api.obis.org/v3/occurrence/grid/" + precisionGeohash + "?scientificname=" + Espece;
		if (Histo) {
			return requeteAPIHisto(parent, couleur, url);
		} else {
			return requeteAPI(parent, couleur, url);
		}
	}

	// Permet de faire une requete à partir nom espece + precision GeoHash + date debut + date Fin
	public static int[] creationRequeteNomEspeceGeohashDebutFin(Group parent, ArrayList<PhongMaterial> couleur,
			String Espece, int precisionGeohash, LocalDate debut, LocalDate fin, boolean Histo) {
		String url = "https://api.obis.org/v3/occurrence/grid/" + precisionGeohash + "?scientificname=" + Espece
				+ "&startdate=" + debut.toString() + "&enddate=" + fin.toString();
		if (Histo) {
			return requeteAPIHisto(parent, couleur, url);
		} else {
			return requeteAPI(parent, couleur, url);
		}
	}

	// Permet d'afficher les quadrilatères sur la terre à partir d'un url de données
	public static int[] requeteAPI(Group parent, ArrayList<PhongMaterial> couleur, String urlDonnees) {
		JSONObject url = readJsonFromUrl(urlDonnees);
		JSONArray resultatRecherche = url.getJSONArray("features");
		// Supprime l'ancien histogramme
		parent.getChildren().clear();
		for (int i = 0; i < resultatRecherche.length(); i++) {
			JSONArray coordonnees = resultatRecherche.getJSONObject(i).getJSONObject("geometry")
					.getJSONArray("coordinates");
			;
			int nombre = resultatRecherche.getJSONObject(i).getJSONObject("properties").getInt("n");
			int max = resultatRecherche.getJSONObject(0).getJSONObject("properties").getInt("n");
			int min = resultatRecherche.getJSONObject(resultatRecherche.length() - 1).getJSONObject("properties")
					.getInt("n");
			int pas = (int) (max - min) / 8;
			int legende = min + pas;
			if (nombre < legende) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(0));
			} else if (nombre < legende + (pas * 1)) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(1));
			} else if (nombre < legende + (pas * 2)) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(2));
			} else if (nombre < legende + (pas * 3)) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(3));
			} else if (nombre < legende + (pas * 4)) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(4));
			} else if (nombre < legende + (pas * 5)) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(5));
			} else if (nombre < legende + (pas * 6)) {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(6));
			} else {
				Forme.AddQuadrilateral(parent,
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(2).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)),
						Forme.geoCoordTo3dCoord((float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(1),
								(float) coordonnees.getJSONArray(0).getJSONArray(0).getDouble(0)),
						couleur.get(7));
			}
		}
		int max = resultatRecherche.getJSONObject(0).getJSONObject("properties").getInt("n");
		int min = resultatRecherche.getJSONObject(resultatRecherche.length() - 1).getJSONObject("properties")
				.getInt("n");
		int pas = (int) (max - min) / 8;
		int legende = min + pas;
		System.out.println("Max :" + String.valueOf(max));
		System.out.println("Min :" + String.valueOf(min));
		int[] leg = { legende, pas };
		return leg;
	}
	
	// Permet d'afficher les histogrammes sur la terre à partir d'un url de données
	public static int[] requeteAPIHisto(Group parent, ArrayList<PhongMaterial> couleur, String urlDonnees) {
		JSONObject url = readJsonFromUrl(urlDonnees);
		JSONArray resultatRecherche = url.getJSONArray("features");
		// Supprime l'ancien histogramme
		parent.getChildren().clear();
		for (int i = 0; i < resultatRecherche.length(); i++) {
			JSONArray coordonnees = resultatRecherche.getJSONObject(i).getJSONObject("geometry")
					.getJSONArray("coordinates");
			;
			int nombre = resultatRecherche.getJSONObject(i).getJSONObject("properties").getInt("n");
			int max = resultatRecherche.getJSONObject(0).getJSONObject("properties").getInt("n");
			int min = resultatRecherche.getJSONObject(resultatRecherche.length() - 1).getJSONObject("properties")
					.getInt("n");
			int pas = (int) (max - min) / 8;
			int legende = min + pas;
			if (nombre < legende) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.025));
				c.setMaterial(couleur.get(0));
				parent.getChildren().add(c);
			} else if (nombre < legende + (pas * 1)) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.05));
				c.setMaterial(couleur.get(1));
				parent.getChildren().add(c);
			} else if (nombre < legende + (pas * 2)) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.075));
				c.setMaterial(couleur.get(2));
				parent.getChildren().add(c);
			} else if (nombre < legende + (pas * 3)) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.1));
				c.setMaterial(couleur.get(3));
				parent.getChildren().add(c);
			} else if (nombre < legende + (pas * 4)) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.125));
				c.setMaterial(couleur.get(4));
				parent.getChildren().add(c);
			} else if (nombre < legende + (pas * 5)) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.15));
				c.setMaterial(couleur.get(5));
				parent.getChildren().add(c);
			} else if (nombre < legende + (pas * 6)) {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.175));
				c.setMaterial(couleur.get(6));
				parent.getChildren().add(c);
			} else {
				float lat = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(1)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(1)) / 2);
				;
				float lon = (float) ((float) (coordonnees.getJSONArray(0).getJSONArray(3).getDouble(0)
						+ (float) coordonnees.getJSONArray(0).getJSONArray(1).getDouble(0)) / 2);
				Point3D origine = Forme.geoCoordTo3dCoord(lat, lon);
				Cylinder c = Forme.createLine(origine, origine.multiply(1.2));
				c.setMaterial(couleur.get(7));
				parent.getChildren().add(c);
			}

		}
		int max = resultatRecherche.getJSONObject(0).getJSONObject("properties").getInt("n");
		int min = resultatRecherche.getJSONObject(resultatRecherche.length() - 1).getJSONObject("properties")
				.getInt("n");
		int pas = (int) (max - min) / 8;
		int legende = min + pas;
		System.out.println("Max :" + String.valueOf(max));
		System.out.println("Min :" + String.valueOf(min));
		int[] leg = { legende, pas };
		return leg;
	}

	
	  // Permet autocompletion de la listeview par nom d'espece dans la barre de recherche
	public static ArrayList<String> autocompletionEspece(String debutNom) {
		ArrayList<String> premiersNoms = new ArrayList<String>();

		JSONArray resultatRecherche = readJsonArrayFromUrl(
				"https://api.obis.org/v3/taxon/complete/verbose/" + debutNom);
		System.out.println("nb objet ds array (espece) : " + resultatRecherche);

		for (int i = 0; i < resultatRecherche.length(); i++) { // les 20 premiers résultats
			premiersNoms.add(resultatRecherche.getJSONObject(i).get("scientificName").toString());
		}
		if(resultatRecherche.length()==0) {
			premiersNoms.add("Aucune espèce");
		}
		return premiersNoms;
	}

	  // Permet avoir toutes les especes pour un geohash
	public static ArrayList<String> getEspeceFromGeohash(String geoHash) {
		ArrayList<String> especes = new ArrayList<String>();

		String urlDonnees = "https://api.obis.org/v3/occurrence?geometry=" + geoHash;
		JSONObject url = readJsonFromUrl(urlDonnees);
		JSONArray resultatRecherche = url.getJSONArray("results");
		for (int i = 0; i < resultatRecherche.length(); i++) {
			if(especes.contains(resultatRecherche.getJSONObject(i).get("scientificName").toString()) == false) {
				especes.add(resultatRecherche.getJSONObject(i).get("scientificName").toString());
			}
		}
		return especes;
	}

	  // Permet avoir infos sur les especes à partir pour un geohash
	public static ArrayList<StringBuilder> getInfoFromEspeceGeoHash(String espece, String geoHash) {
		ArrayList<String> especeInfo = new ArrayList<String>();
		ArrayList<StringBuilder> info = new ArrayList<StringBuilder>();
		StringBuilder donnees = new StringBuilder();
		donnees.append("Geohash: " + geoHash + "\n\n");

		especeInfo = getEspeceFromGeohash(geoHash);

		for (int i = 0; i < especeInfo.size(); i++) {
			donnees.append(getInfoFromEspece(especeInfo.get(i).replace(" ", "%20")));
		}
		info.add(donnees);
		return info;
	}

	  // Permet avoir les infos d'une espece
	public static ArrayList<StringBuilder> getInfoFromEspece(String espece) {
		if(espece.charAt(0) >= 'a' && espece.charAt(0) <= 'z') {
			String firstLtr = espece.substring(0, 1);
	        String restLtrs = espece.substring(1, espece.length());
	      
	        firstLtr = firstLtr.toUpperCase();
	        espece = firstLtr + restLtrs;
		}
		String urlDonnees = "https://api.obis.org/v3/checklist?scientificname=" + espece;
		JSONObject json = readJsonFromUrl(urlDonnees);
		JSONArray resultatRecherche = json.getJSONArray("results");

		System.out.println(resultatRecherche.length());
		ArrayList<StringBuilder> info = new ArrayList<StringBuilder>();
		StringBuilder donnees = new StringBuilder();
		String especeAvecEspace = espece.replace("%20", " ");
		donnees.append("Espèce: " + especeAvecEspace + "\n\n");
		if(resultatRecherche.length() > 0) {
			for (int i = 0; i < resultatRecherche.length(); i++) {
				if (resultatRecherche.getJSONObject(i).get("scientificName").equals(espece)) {
					String scientificName = resultatRecherche.getJSONObject(i).get("scientificName").toString();
					String[] recordedBy = resultatRecherche.getJSONObject(i).get("scientificNameAuthorship").toString().split(",");
					recordedBy[0] = recordedBy[0].replaceAll(" ","");
					if(recordedBy[0].contains("(")) {
						recordedBy[0] = recordedBy[0].replaceAll("(","");
					}
					recordedBy[1] = recordedBy[1].replaceAll(" ","");
					if(recordedBy[1].contains(")")) {
						recordedBy[1] = recordedBy[1].replaceAll(")","");
					}
					String order = "N/A";
					if (resultatRecherche.getJSONObject(i).toString().contains("\"order\"")) {
						order = resultatRecherche.getJSONObject(i).get("order").toString();
					}
					String classe = "N/A";
					if (resultatRecherche.getJSONObject(i).toString().contains("\"class\"")) {
						classe = resultatRecherche.getJSONObject(i).get("class").toString();
					}
					String species = "N/A";
					if (resultatRecherche.getJSONObject(i).toString().contains("\"species\"")) {
						species = resultatRecherche.getJSONObject(i).get("species").toString();
					}

					donnees.append("Scientific name: " + scientificName + "\n" + "Order: " + order + "\n" + "Class: "
						+ classe + "\n" + "Recorded by: " + recordedBy[0] + " in " + recordedBy[1] +"\n" + "Species: " + species + "\n");
					donnees.append("\n");
				}
			}
		}
		if(donnees.length()<50) {
			donnees.append("Scientific name: " + especeAvecEspace + "\n" + "Order: N/A" + "\n" + "Class: N/A" + "\n" 
					+ "Recorded by: N/A" +"\n" + "Species: N/A\n");
			donnees.append("\n");
		}
		info.add(donnees);
		System.out.println(info);
		return info;
	}
	
	// Permet d'avoir les coordonnées GPS à partir d'un GéoHash
	public static String getGPSFromGeohash(String geoHash) {
		String url = "https://api.obis.org/v3/occurrence/centroid?geometry="+geoHash;
		JSONObject json = readJsonFromUrl(url);
		String lat = json.get("lat").toString();
		String lon = json.get("lon").toString();
		BigDecimal lat1 = new BigDecimal(Double.parseDouble(lat)).setScale(4, RoundingMode.HALF_UP);
		BigDecimal lon1 = new BigDecimal(Double.parseDouble(lon)).setScale(4, RoundingMode.HALF_UP);
		String coordGPS = String.valueOf(lat1) + " ; " + String.valueOf(lon1);
		return coordGPS;
	}
}
