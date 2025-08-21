package house.entity;

import common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import member.entity.HostMember;

@Entity
@Getter
@Setter
public class House extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "host",
		foreignKey = @ForeignKey(
			name = "fk_House_HostMember",
			foreignKeyDefinition = """
					foreign key (host)
					   references HostMember(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	private HostMember host;

	@Column(length = 16, nullable = false)
	private String title;

	@Column(length = 64, nullable = false)
	private String address;

	@Column(nullable = false)
	private Short capacity;

	@Column(nullable = false)
	private Short areaSize;

	@Column(length = 512, nullable = true)
	private String description;

	@Column(nullable = false)
	private Boolean isHomestay;

	@Column(length = 31, nullable = true)
	private String ownerPhone;
}
