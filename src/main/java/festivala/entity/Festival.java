package festivala.entity;

import common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Festival extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 31, nullable = false)
	private String date;

	@Column(length = 64, nullable = false)
	private String region;

	@Column(length = 16, nullable = false)
	private String title;

	@Column(length = 31, nullable = false)
	private String url;

	@Column(nullable = false)
	private byte poster;

	@Column(length = 512, nullable = false)
	private String description;
}
