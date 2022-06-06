package com.Jvnyor.animestore.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.Jvnyor.animestore.conn.ConnectionFactory;
import com.Jvnyor.animestore.dominio.Anime;
import com.Jvnyor.animestore.dominio.Producer;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AnimeRepository {

	public static List<Anime> findByName(String name) {
		log.info("Finding Animes by name '{}'", name);
		List<Anime> animes = new ArrayList<>();
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = createPreparedStatementFindByName(conn, name);
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				Producer producer = Producer.builder()
						.name(rs.getString("producer_name"))
						.id(rs.getInt("producer_id"))
						.build();
				Anime anime = Anime.builder()
						.id(rs.getInt("id"))
						.name(rs.getString("name"))
						.episodes(rs.getInt("episodes"))
						.producer(producer)
						.build();
				animes.add(anime);
			}
		} catch (SQLException e) {
			log.error("Error while trying to update anime", e);
		}
		return animes;
	}

	private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name)
			throws SQLException {
		String sql = "SELECT a.id, a.name, a.episodes, a.producer_id, p.name as 'producer_name' FROM anime_store.anime a INNER JOIN anime_store.producer p on a.producer_id = p.id WHERE a.name LIKE ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, String.format("%%%s%%", name));
		return ps;
	}

	public static Optional<Anime> findById(Integer id) {
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = createPreparedStatementFindById(conn, id);
				ResultSet rs = ps.executeQuery()) {
			if (!rs.next()) {
				return Optional.empty();
			}
			Producer producer = Producer.builder()
					.name(rs.getString("producer_name"))
					.id(rs.getInt("producer_id"))
					.build();
			return Optional.of(Anime.builder()
					.id(rs.getInt("id"))
					.name(rs.getString("name"))
					.episodes(rs.getInt("episodes"))
					.producer(producer)
					.build());
		} catch (SQLException e) {
			log.error("Error while trying to find anime by the id '{}'", id, e);
		}
		return Optional.empty();
	}

	private static PreparedStatement createPreparedStatementFindById(Connection conn, Integer id) throws SQLException {
		String sql = "SELECT a.id, a.name, a.episodes, a.producer_id, p.name as 'producer_name' FROM anime_store.anime a INNER JOIN anime_store.producer p on a.producer_id = p.id WHERE a.id = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		return ps;
	}

	public static void delete(int id) {
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = createPreparedStatementDelete(conn, id)) {
			ps.execute();
			log.info("Deleted anime '{}' from the database", id);
		} catch (SQLException e) {
			log.error("Error while trying to delete anime '{}'", id, e);
		}
	}

	private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id) throws SQLException {
		String sql = "DELETE FROM `anime_store`.`anime` WHERE (`id` = ?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		return ps;
	}

	public static void save(Anime anime) {
		log.info("Saving Anime '{}'", anime);
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = createPreparedStatementSave(conn, anime);) {
			ps.execute();
		} catch (SQLException e) {
			log.error("Error while trying to save anime '{}'", anime.getName(), e);
		}
	}

	private static PreparedStatement createPreparedStatementSave(Connection conn, Anime anime) throws SQLException {
		String sql = "INSERT INTO `anime_store`.`anime` (name, episodes, producer_id) VALUES (?, ?, ?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, anime.getName());
		ps.setInt(2, anime.getEpisodes());
		ps.setInt(3, anime.getProducer().getId());
		return ps;
	}

	public static void update(Anime anime) {
		log.info("Updating producer '{}'", anime);
		try (Connection conn = ConnectionFactory.getConnection();
				PreparedStatement ps = createPreparedStatementUpdate(conn, anime);) {
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("Error while trying to update anime '{}'", anime.getId(), e);
		}
	}

	private static PreparedStatement createPreparedStatementUpdate(Connection conn, Anime anime) throws SQLException {
		String sql = "UPDATE `anime_store`.`anime` SET `name` = ?, `episodes` = ? WHERE (`id` = ?);";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, anime.getName());
		ps.setInt(2, anime.getEpisodes());
		ps.setInt(3, anime.getId());
		return ps;
	}
}