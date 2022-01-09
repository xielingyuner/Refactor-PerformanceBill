package refactoring.performancebill.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import refactoring.performancebill.domain.model.perfbill.PerfBill;
import refactoring.performancebill.domain.model.perfbill.PerfBillRepository;
import refactoring.performancebill.domain.model.perfsummary.Perf;
import refactoring.performancebill.domain.model.perfsummary.PerfSummary;
import refactoring.performancebill.domain.model.play.Play;
import refactoring.performancebill.domain.model.play.PlayRepository;

import javax.transaction.Transactional;

@Service
public class PerfBillService {
    @Autowired
    PerfBillRepository repository;

    @Autowired
    PlayRepository playRepository;

    @Transactional
    public PerfBill createBill(@RequestBody PerfSummary perfSummary) {
        int totalAmount = 0;
        int volumeCredits = 0;
        PerfBill bill = new PerfBill(perfSummary.getCustomer());
        for (Perf perf : perfSummary.getPerfs()) {
            Play play = playRepository.findById(perf.getPlayId());

            Calculator calculator = buildCalculator(play);
            volumeCredits += calculator.calCredits(perf, play);
            totalAmount += calculator.calAmount(perf, play);

            bill.addItem(play.getName(), calculator.calAmount(perf, play), perf.getAudience());
        }

        bill.setTotalAmount(totalAmount);
        bill.setVolumeCredits(volumeCredits);

        return repository.save(bill);
    }

    private Calculator buildCalculator(Play play) {
        Calculator calculator;
        if ("tragedy".equals(play.getType())) {
            calculator = new TragedyCalculator();
        } else {
            calculator = new ComedyCalculator();
        }
        return calculator;
    }

}
