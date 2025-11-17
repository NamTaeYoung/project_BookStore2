package com.boot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boot.dto.BookDTO;
import com.boot.dto.GenreDTO;


@Mapper
public interface BookMapper {
	 // 전체 도서 목록
    List<BookDTO> getAllBooks();

    // 전체 장르 목록
    List<GenreDTO> getAllGenres();

    <!-- 특정 장르별 도서 목록 -->
	<select id="getBooksByGenre" parameterType="int"
		resultType="com.boot.dto.BookDTO">
		SELECT *
		FROM book
		WHERE genre_id = #{genre_id}
		ORDER BY book_id
	</select>
	<!-- 페이징 목록 -->



<!-- 전체 개수 -->
<select id="countBooks" resultType="int">
    SELECT COUNT(*) FROM Book
</select>
}
