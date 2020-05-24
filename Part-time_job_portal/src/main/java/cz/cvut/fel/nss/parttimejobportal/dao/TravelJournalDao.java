package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.TravelJournal;
import org.springframework.stereotype.Repository;

@Repository
public class TravelJournalDao extends BaseDao<TravelJournal> {
    protected TravelJournalDao() {
        super(TravelJournal.class);
    }


}
