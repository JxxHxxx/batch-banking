package com.jxx.banking.reader;

import com.jxx.banking.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.transform.FieldSet;

// ExitStatus 가 리더의 상태에 묶여 있기 때문에 커스텀 리더기를 구현한다.
@Slf4j
public class TransactionReader implements ItemStreamReader<Transaction> {

    private ItemStreamReader<FieldSet> fieldSetReader;
    private int recordCount = 0;
    private int expectedRecordCount = 0;

    public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

    @Override
    public Transaction read() throws Exception {
        return process(fieldSetReader.read());
    }

    private Transaction process(FieldSet fieldSet) {
        Transaction result = null;

        if (fieldSet != null) {
            // 데이터 레코드
            if (fieldSet.getFieldCount() > 1) {
                result = new Transaction();
                result.setAccountNumber(fieldSet.readString(0));
                result.setTimestamp(fieldSet.readDate(1, "yyyy-MM-DD HH:mm:ss"));
                result.setAmount(fieldSet.readDouble(2));

                recordCount++;
            }
            // 푸터 레코드
            else {
                log.info("여기 쑤심?");
                expectedRecordCount = fieldSet.readInt(0);
            }

        }
        return result; // null 을 리턴하면 파일 처리가 완료됐음을 의미
    }

    public void setFieldSetReader(ItemStreamReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution execution) {
        if (recordCount == expectedRecordCount) {
            log.info("recordCount == expectedRecordCount");
            return execution.getExitStatus();
        }
        else { // 레코드 수와 실제 레코드 수가 일치하지 않다면 잡을 중지
            log.info("recordCount != expectedRecordCount rc {} , erc {}", recordCount, expectedRecordCount);
            return ExitStatus.STOPPED;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        fieldSetReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        fieldSetReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        fieldSetReader.close();
    }
}
