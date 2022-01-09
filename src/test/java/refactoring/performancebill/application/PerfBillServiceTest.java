package refactoring.performancebill.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import refactoring.performancebill.domain.model.perfbill.PerfBill;
import refactoring.performancebill.domain.model.perfbill.PerfBillRepository;
import refactoring.performancebill.domain.model.perfsummary.PerfSummary;
import refactoring.performancebill.domain.model.play.Play;
import refactoring.performancebill.domain.model.play.PlayRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PerfBillServiceTest {

    @Mock
    private PerfBillRepository mockBillRepository;

    @Mock
    PlayRepository mockPlayRepository;

    @InjectMocks
    PerfBillService service;

    static final Play HAMLET = new Play("hamlet"
            , "Hamlet"
            , "tragedy");
    static final Play AS_LIKE = new Play("as-like"
            , "As You Like It"
            , "comedy");
    static final Play OTHELLO = new Play("othello"
            , "Othello"
            , "tragedy");

    @Test
    void should_create_bill_correct_when_calculate_performance_bill_given_an_tragedy_and_audience_less_than_30() {
        doReturn(HAMLET).when(mockPlayRepository).findById("hamlet");
        verifyCreateBillForOnePerformance(
                "hamlet"
                , 10
                , 40000
                , 0
                , "Hamlet");
    }

    @Test
    void should_create_bill_correct_when_calculate_performance_bill_given_an_tragedy_and_audience_more_than_30() {
        doReturn(OTHELLO).when(mockPlayRepository).findById("othello");
        verifyCreateBillForOnePerformance(
                "othello"
                , 50
                , 60000
                , 20
                , "Othello");


    }

    @Test
    void should_create_bill_correct_when_calculate_performance_bill_given_an_comedy_audience_less_than_20() {
        doReturn(AS_LIKE).when(mockPlayRepository).findById("as-like");
        verifyCreateBillForOnePerformance(
                "as-like"
                , 10
                , 33000
                , 2
                , "As You Like It");


    }

    @Test
    void should_create_bill_correct_when_calculate_performance_bill_given_an_comedy_audience_more_than_20() {
        doReturn(AS_LIKE).when(mockPlayRepository).findById("as-like");

        verifyCreateBillForOnePerformance(
                "as-like"
                , 30
                , 54000
                , 6
                , "As You Like It");


    }


    private void verifyCreateBillForOnePerformance(
            String playId
            , int audience
            , int expectedAmount
            , int expectedVolumeCredits
            , String expectedPlayName) {

        final String company = "AAA";

        PerfSummary summary = new PerfSummary(company);
        summary.addPerformance(playId, audience);

        service.createBill(summary);

        ArgumentCaptor<PerfBill> argument = ArgumentCaptor.forClass(PerfBill.class);
        verify(mockBillRepository).save(argument.capture());
        PerfBill actual = argument.getValue();

        assertThat(actual.getCustomer()).isEqualTo(company);
        assertThat(actual.getVolumeCredits()).isEqualTo(expectedVolumeCredits);
        assertThat(actual.getItems()).hasSize(1)
                .extracting("name", "amount", "audience")
                .contains(tuple(expectedPlayName, expectedAmount, audience));
    }
}