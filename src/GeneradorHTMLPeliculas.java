import java.io.*;
import java.nio.file.*;
import java.util.*;


 //Esta clase se encarga de generar archivos HTML a partir de una lista de películas contenida en un archivo CSV.

public class GeneradorHTMLPeliculas {

    /**
     * Método principal que ejecuta el programa. Lee un archivo CSV con datos de películas, una plantilla HTML
     * y genera archivos HTML para cada película.
     */
    public static void main(String[] args) {
        // Ruta completa al archivo CSV y al archivo de plantilla HTML
        String archivoCSV = "C:/Users/Alberto/IdeaProjects/Reto1 Acceso a datos/peliculas.csv";
        String plantillaHTML = "C:/Users/Alberto/IdeaProjects/Reto1 Acceso a datos/template.html.txt";
        String carpetaSalida = "C:/Users/Alberto/IdeaProjects/Reto1 Acceso a datos/salida";

        // Imprimir las rutas para depuración
        System.out.println("Ruta de archivo CSV: " + archivoCSV);
        System.out.println("Ruta de archivo de plantilla: " + plantillaHTML);
        System.out.println("Ruta de salida: " + carpetaSalida);

        try {
            // Verificar si el archivo CSV existe
            File archivoCSVFile = new File(archivoCSV);
            if (!archivoCSVFile.exists()) {
                System.err.println("El archivo CSV no existe: " + archivoCSV);
                return;
            }

            // Leer las películas del archivo CSV
            List<String[]> peliculas = leerCSV(archivoCSV);

            // Verificar si el archivo de plantilla existe
            File plantillaFile = new File(plantillaHTML);
            if (!plantillaFile.exists()) {
                System.err.println("El archivo de plantilla HTML no existe: " + plantillaHTML);
                return;
            } else {
                System.out.println("Archivo de plantilla encontrado en: " + plantillaHTML);
            }

            // Leer la plantilla HTML
            String template = new String(Files.readAllBytes(Paths.get(plantillaHTML)));

            // Crear carpeta de salida si no existe
            File directorioSalida = new File(carpetaSalida);
            if (!directorioSalida.exists()) {
                directorioSalida.mkdir();
            } else {
                // Vaciar la carpeta de salida
                for (File archivo : directorioSalida.listFiles()) {
                    archivo.delete();
                }
            }

            // Generar un archivo HTML para cada película
            for (String[] pelicula : peliculas) {
                generarArchivoHTML(pelicula, template, carpetaSalida);
            }

            System.out.println("Archivos HTML generados correctamente en la carpeta 'salida'.");

        } catch (Exception e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lee el archivo CSV que contiene los datos de las películas y los convierte en una lista de arrays de String.
     * Cada línea del archivo CSV representa una película y se divide en 5 campos: ID, título, año, director y género.
     *
     * @param rutaArchivoCSV Ruta del archivo CSV que se va a leer.
     * @return Lista de arrays de String, donde cada array representa una película.
     * @throws IOException Si ocurre un error al leer el archivo CSV.
     */
    public static List<String[]> leerCSV(String rutaArchivoCSV) throws IOException {
        List<String[]> peliculas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en columnas separadas por comas
                String[] datos = linea.split(",");
                // Validar que se tienen todos los campos requeridos
                if (datos.length != 5) {
                    System.err.println("Datos incompletos en la línea: " + linea);
                    continue;
                }
                peliculas.add(datos);
            }
        }
        return peliculas;
    }

    /**
     * Genera un archivo HTML basado en una plantilla HTML, reemplazando las variables con los datos de la película.
     * Crea un archivo HTML por cada película en la carpeta de salida.
     *
     * @param pelicula      Array de String que contiene los datos de una película: ID, título, año, director, género.
     * @param template      Plantilla HTML con variables a reemplazar.
     * @param carpetaSalida Carpeta donde se guardarán los archivos HTML generados.
     * @throws IOException Si ocurre un error al escribir el archivo HTML.
     */
    public static void generarArchivoHTML(String[] pelicula, String template, String carpetaSalida) throws IOException {
        // Reemplazar las variables de la plantilla con los datos de la película
        String htmlContenido = template
                .replace("%%1%%", pelicula[0])   // ID
                .replace("%%2%%", pelicula[1])   // Título
                .replace("%%3%%", pelicula[2])   // Año
                .replace("%%4%%", pelicula[3])   // Director
                .replace("%%5%%", pelicula[4]);  // Género

        // Crear el nombre del archivo HTML, por ejemplo: "El_Padrino - 101.html"
        String nombreArchivo = pelicula[1].replace(" ", "_") + " - " + pelicula[0] + ".html";

        // Crear el archivo HTML en la carpeta de salida
        File archivoSalida = new File(carpetaSalida, nombreArchivo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {
            writer.write(htmlContenido);
        }
    }
}
