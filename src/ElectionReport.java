public class ElectionReport {
    private CSVFileReader candidateData;

    enum CandidateSituation {
        ELECTED,
        NOT_ELECTED;
    }

    private CSVFileReader votingData;

    public ElectionReport(int cityCode, String candidatePath, String votingPath) {
        try {
            candidateData = new CSVFileReader(candidatePath);
            votingData = new CSVFileReader(votingPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
