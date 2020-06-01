package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.JobJournal;
import org.springframework.stereotype.Repository;

@Repository
public class JobJournalDao extends BaseDao<JobJournal> {
    protected JobJournalDao() {
        super(JobJournal.class);
    }


}
