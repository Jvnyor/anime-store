package com.Jvnyor.animestore.service;

import java.util.Optional;
import java.util.Scanner;

import com.Jvnyor.animestore.dominio.Anime;
import com.Jvnyor.animestore.dominio.Producer;
import com.Jvnyor.animestore.repository.AnimeRepository;

public class AnimeService {

	private static final Scanner SCANNER = new Scanner(System.in);

	public static void menu(int op) {
		switch (op) {
		case 1 -> findByName();
		case 2 -> delete();
		case 3 -> save();
		case 4 -> update();
		}
	}

	private static void findByName() {
		System.out.println("Type the name or empty to all");
		String name = SCANNER.nextLine();
		AnimeRepository.findByName(name).forEach(a -> System.out.printf("[%d] - %s %d %s%n", a.getId(), a.getName(),
				a.getEpisodes(), a.getProducer().getName()));
	}

	private static void delete() {
		System.out.println("Type the id of the anime you want to delete");
		int id = Integer.parseInt(SCANNER.nextLine());
		System.out.println("Are you sure? Y/N");
		String choice = SCANNER.nextLine();
		if ("y".equalsIgnoreCase(choice)) {
			AnimeRepository.delete(id);
		}
	}

	private static void save() {
		System.out.println("Type the name of the anime");
		String name = SCANNER.nextLine();
		System.out.println("Type the number of episodes");
		int episodes = Integer.parseInt(SCANNER.nextLine());
		System.out.println("Type the id of the producer");
		Integer producerId = Integer.parseInt(SCANNER.nextLine());
		AnimeRepository.save(Anime.builder()
				.name(name)
				.episodes(episodes)
				.producer(Producer.builder().id(producerId).build())
				.build());
	}

	private static void update() {
		System.out.println("Type the id of the anime you want to update");
		Optional<Anime> animeOptional = AnimeRepository.findById(Integer.parseInt(SCANNER.nextLine()));
		if (animeOptional.isEmpty()) {
			System.out.println("Anime not found");
		}
		Anime animeFromDB = animeOptional.get();
		System.out.println("Anime found " + animeFromDB);
		System.out.println("Type the new name or enter to keep the same");
		String name = SCANNER.nextLine();
		System.out.println("Type the number of episodes");
		int episodes = Integer.parseInt(SCANNER.nextLine());
		name = name.isEmpty() ? animeFromDB.getName() : name;
		Anime animeToUpdate = Anime.builder()
				.id(animeFromDB.getId())
				.name(name)
				.episodes(episodes)
				.producer(animeFromDB.getProducer())
				.build();
		AnimeRepository.update(animeToUpdate);
	}
}