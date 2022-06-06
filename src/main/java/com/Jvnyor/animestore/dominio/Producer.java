package com.Jvnyor.animestore.dominio;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public final class Producer {
	Integer id;
	String name;
}