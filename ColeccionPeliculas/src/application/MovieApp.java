package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MovieApp extends Application {
	// creamos una lista observable para almacenar las peliculas
	private ObservableList<Movie> movies = FXCollections.observableArrayList();

	// creamos una tabla para mostrar las peliculas
	private TableView<Movie> table = new TableView<>();

	// creamos los campos para ingresar los detalles de la pelicula
	private TextField titleField = new TextField();
	private TextField yearField = new TextField();
	private ToggleGroup genreGroup = new ToggleGroup();
	private RadioButton actionButton = new RadioButton("Acción");
	private RadioButton comedyButton = new RadioButton("Comedia");
	private RadioButton dramaButton = new RadioButton("Drama");

	// creamos los botones para editar y eliminar peliculas
	private Button addButton = new Button("Añadir");
	private Button editButton = new Button("Editar");
	private Button deleteButton = new Button("Eliminar");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// configuramos la tabla para mostrar las peliculas
		TableColumn<Movie, String> titleCol = new TableColumn<>("Título");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

		TableColumn<Movie, Integer> yearCol = new TableColumn<>("Año");
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

		TableColumn<Movie, String> genreCol = new TableColumn<>("Género");
		genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

		table.setItems(movies);
		table.getColumns().addAll(titleCol, yearCol, genreCol);

		// configuramos los botones de género como un grupo de opciones mutuamente
		// excluyentes
		actionButton.setToggleGroup(genreGroup);
		comedyButton.setToggleGroup(genreGroup);
		dramaButton.setToggleGroup(genreGroup);

		// creamos un contenedor para los campos de entrada y botones
		HBox inputContainer = new HBox(10);
		inputContainer.setPadding(new Insets(10));
		inputContainer.getChildren().addAll(titleField, yearField, actionButton, comedyButton, dramaButton, addButton,
				editButton, deleteButton);
		// creamos un contenedor principal para la tabla y el contenedor de entrada
		VBox root = new VBox(10);
		root.setPadding(new Insets(10));
		root.getChildren().addAll(table, inputContainer);

		// configuramos el botón de añadir para agregar una película a la lista cuando
		// se presiona
		addButton.setOnAction(event -> {
			// obtenemos los valores de los campos de entrada
			String title = titleField.getText();
			int year = Integer.parseInt(yearField.getText());
			String genre = ((RadioButton) genreGroup.getSelectedToggle()).getText();

			// agregamos una nueva película a la lista
			movies.add(new Movie(title, year, genre));

			// limpiamos los campos de entrada
			titleField.clear();
			yearField.clear();
			genreGroup.selectToggle(null);
		});

		// configuramos el botón de editar para actualizar una película seleccionada en
		// la tabla
		editButton.setOnAction(event -> {
			// obtenemos la película seleccionada en la tabla
			Movie selectedMovie = table.getSelectionModel().getSelectedItem();

			// si no hay ninguna película seleccionada, mostramos un mensaje de error
			if (selectedMovie == null) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor selecciona una película para editar.");
				alert.show();
				return;
			}

			// actualizamos los valores de la película seleccionada con los valores de los
			// campos de entrada
			selectedMovie.setTitle(titleField.getText());
			selectedMovie.setYear(Integer.parseInt(yearField.getText()));
			selectedMovie.setGenre(((RadioButton) genreGroup.getSelectedToggle()).getText());

			// actualizamos la tabla para reflejar los cambios
			table.refresh();

			// limpiamos los campos de entrada
			titleField.clear();
			yearField.clear();
			genreGroup.selectToggle(null);
		});

		// configuramos el botón de eliminar para eliminar una película seleccionada de
		// la tabla
		deleteButton.setOnAction(event -> {
			// obtenemos la película seleccionada en la tabla
			Movie selectedMovie = table.getSelectionModel().getSelectedItem();

			// si no hay ninguna película seleccionada, mostramos un mensaje de error
			if (selectedMovie == null) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor selecciona una película para eliminar.");
				alert.show();
				return;
			}
			// eliminamos la película seleccionada de la lista
			movies.remove(selectedMovie);

			// actualizamos la tabla para reflejar los cambios
			table.refresh();

			// limpiamos los campos de entrada
			titleField.clear();
			yearField.clear();
			genreGroup.selectToggle(null);
		});

		// configuramos el evento de selección de la tabla para mostrar los detalles de
		// la película seleccionada en los campos de entrada
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				titleField.setText(newValue.getTitle());
				yearField.setText(Integer.toString(newValue.getYear()));
				if (newValue.getGenre().equals("Acción")) {
					genreGroup.selectToggle(actionButton);
				} else if (newValue.getGenre().equals("Comedia")) {
					genreGroup.selectToggle(comedyButton);
				} else {
					genreGroup.selectToggle(dramaButton);
				}
			}
		});

		// mostramos la escena
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Lista de Películas");
		primaryStage.show();
	}

	// clase interna para representar las peliculas
	public static class Movie {
		private String title;
		private int year;
		private String genre;

		public Movie(String title, int year, String genre) {
			this.title = title;
			this.year = year;
			this.genre = genre;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public String getGenre() {
			return genre;
		}

		public void setGenre(String genre) {
			this.genre = genre;
		}
	}
}