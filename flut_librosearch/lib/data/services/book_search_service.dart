import 'dart:convert';
import 'package:http/http.dart' as http;

class BookSearchService {
  // URL base de la API de Google Books
  static const String _googleBooksApiUrl = 'https://www.googleapis.com/books/v1/volumes';
  
  /// Busca libros usando directamente la API de Google Books
  Future<BookSearchResult> searchBooks(String query) async {
    try {
      print('Buscando libros con query: $query');
      
      // Construir la URL con el parámetro de búsqueda
      final uri = Uri.parse(_googleBooksApiUrl).replace(queryParameters: {
        'q': query,
        'maxResults': '20', // Limitar resultados
        'printType': 'books', // Solo libros
        'langRestrict': 'es', // Opcional: solo libros en español
      });
      
      print('URL de búsqueda: $uri');
      
      final response = await http.get(
        uri,
        headers: {
          'Accept': 'application/json',
        },
      ).timeout(const Duration(seconds: 15));
      
      print('Respuesta de búsqueda: ${response.statusCode}');
      
      if (response.statusCode == 200) {
        try {
          // Parsear la respuesta JSON
          final data = json.decode(response.body);
          
          // Extraer los libros de la respuesta
          List<Book> books = [];
          if (data['items'] != null) {
            books = (data['items'] as List)
                .map((item) => Book.fromJson(item))
                .toList();
          }
          
          print('Libros encontrados: ${books.length}');
          return BookSearchResult.success(books);
          
        } catch (jsonError) {
          print('Error parseando JSON: $jsonError');
          return BookSearchResult.failure('Error en el formato de respuesta de Google Books');
        }
      } else if (response.statusCode == 403) {
        return BookSearchResult.failure('Cuota de la API excedida. Intenta más tarde.');
      } else {
        return BookSearchResult.failure('Error en la búsqueda (${response.statusCode})');
      }
      
    } catch (e) {
      print('Error en búsqueda: $e');
      
      if (e.toString().contains('TimeoutException') || 
          e.toString().contains('Connection timed out')) {
        return BookSearchResult.failure(
          'Tiempo de espera agotado. Verifica tu conexión a internet.'
        );
      } else {
        return BookSearchResult.failure('Error de búsqueda: ${e.toString()}');
      }
    }
  }
}

/// Clase para encapsular el resultado de la búsqueda
class BookSearchResult {
  final bool isSuccess;
  final List<Book>? books;
  final String? errorMessage;

  BookSearchResult._({
    required this.isSuccess, 
    this.books, 
    this.errorMessage
  });

  factory BookSearchResult.success(List<Book> books) => 
      BookSearchResult._(isSuccess: true, books: books);
  
  factory BookSearchResult.failure(String message) => 
      BookSearchResult._(isSuccess: false, errorMessage: message);
}

/// Modelo de datos para un libro
class Book {
  final String? id;
  final String title;
  final List<String> authors;
  final String? description;
  final String? imageUrl;
  final String? publishedDate;
  final String? publisher;
  final int? pageCount;
  final String? language;
  final String? previewLink;
  final String? infoLink;

  Book({
    this.id,
    required this.title,
    required this.authors,
    this.description,
    this.imageUrl,
    this.publishedDate,
    this.publisher,
    this.pageCount,
    this.language,
    this.previewLink,
    this.infoLink,
  });

  factory Book.fromJson(Map<String, dynamic> json) {
    final volumeInfo = json['volumeInfo'] ?? {};
    
    return Book(
      id: json['id'],
      title: volumeInfo['title'] ?? 'Título no disponible',
      authors: List<String>.from(volumeInfo['authors'] ?? ['Autor desconocido']),
      description: volumeInfo['description'],
      imageUrl: volumeInfo['imageLinks']?['thumbnail']?.toString().replaceAll('http:', 'https:'),
      publishedDate: volumeInfo['publishedDate'],
      publisher: volumeInfo['publisher'],
      pageCount: volumeInfo['pageCount'],
      language: volumeInfo['language'],
      previewLink: volumeInfo['previewLink'],
      infoLink: volumeInfo['infoLink'],
    );
  }

  @override
  String toString() {
    return 'Book{id: $id, title: $title, authors: $authors}';
  }
}