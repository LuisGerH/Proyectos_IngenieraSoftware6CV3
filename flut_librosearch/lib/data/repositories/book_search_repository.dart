import '../services/book_search_service.dart';

class BookSearchRepository {
  final BookSearchService _bookSearchService = BookSearchService();

  /// Busca libros usando el servicio de Google Books
  Future<BookSearchResult> searchBooks(String query) async {
    if (query.trim().isEmpty) {
      return BookSearchResult.failure('Por favor ingresa un término de búsqueda');
    }
    
    // Validar que la consulta tenga al menos 2 caracteres
    if (query.trim().length < 2) {
      return BookSearchResult.failure('La búsqueda debe tener al menos 2 caracteres');
    }
    
    return await _bookSearchService.searchBooks(query.trim());
  }
}