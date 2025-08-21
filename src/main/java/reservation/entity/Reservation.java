package reservation.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import common.entity.BaseEntity;
import house.entity.House;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import member.entity.HanaMember;
import member.entity.HostMember;

@Entity
public class Reservation extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private short personCnt;

	@Column(nullable = false)
	private Boolean isFarm = true;

	@ManyToOne
	@JoinColumn(name = "member",
		foreignKey = @ForeignKey(
			name = "fk_Reservation_HanaMember",
			foreignKeyDefinition = """
					foreign key (host)
					   references HostMember(id)
					    on DELETE cascade on UPDATE cascade
				"""
		)
	)
	private HanaMember member;

	@OneToOne(optional = false)
	@JoinColumn(name = "house",
		foreignKey = @ForeignKey(name = "fk_Reservation_House"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private House house;
}
