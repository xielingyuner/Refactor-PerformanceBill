package refactoring.performancebill.application;

import refactoring.performancebill.domain.model.perfsummary.Perf;
import refactoring.performancebill.domain.model.play.Play;

public class ComedyCalculator extends Calculator {
    @Override
    public int calAmount(Perf perf, Play play) {
        int currentAmount = 30000;
        if (perf.getAudience() > 20) {
            currentAmount += 10000 + 500 * (perf.getAudience() - 20);
        }
        currentAmount += 300 * perf.getAudience();
        return currentAmount;
    }

    @Override
    public int calCredits(Perf perf, Play play) {
        int currentCredits = Math.max(perf.getAudience() - 30, 0);
        currentCredits += Math.floorDiv(perf.getAudience(), 5);
        return currentCredits;
    }
}
