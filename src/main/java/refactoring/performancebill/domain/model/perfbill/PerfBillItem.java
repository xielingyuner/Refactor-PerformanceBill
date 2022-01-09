package refactoring.performancebill.domain.model.perfbill;

import javax.persistence.*;

@Entity
@Table(name = "performance_bill_item")
public class PerfBillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private int amount;

    @Column(name = "audience")
    private int audience;

    public PerfBillItem(String name, int amount, int audience) {
        this.name = name;
        this.amount = amount;
        this.audience = audience;
    }
}
