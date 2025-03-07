#include "candidate.hpp"

map<int, Candidate> CreateCandidates(
    const vector<vector<string> > &values,
    const map<int, PoliticalParty> &politicalParties)
    {

    }

int Candidate::getCityCode() {
    return cityCode;
}

int Candidate::getJobCode() {
    return jobCode;
}

int Candidate::getCandidateNumber() {
    return candidateNumber;
}

string Candidate::getCandidateName() {
    return candidateName;
}

int Candidate::getPartyNumber() {
    return partyNumber;
}

string Candidate::getPartyAcronym() {
    return partyAcronym;
}

int Candidate::getFederationNumber() {
    return federationNumber;
}

tm Candidate::getBirthDate() {
    return birthDate;
}

CandidateSituation Candidate::getCandidateSituation() {
    return candidateSituation;
}

Gender Candidate::getGender() {
    return gender;
}

int Candidate::getCandidateVotes() {
    return candidateVotes;
}

void Candidate::incCandidateVotes(const int &votes) {
    candidateVotes += votes;
}

void Candidate::setCandidateAge(const tm &electionDate) {
    int age = electionDate.tm_year - birthDate.tm_year;

    // if the birthday hasnt happened in the year yet
    if (electionDate.tm_mon < birthDate.tm_mon ||
        (electionDate.tm_mon == birthDate.tm_mon && electionDate.tm_mday < birthDate.tm_mday)) {
        age--;
    }
    candidateAge = age;
}

long Candidate::getCandidateAge() {
    return candidateAge;
}

int Candidate::getMostVotedPosition() {
    return mostVotedPosition;
}

void Candidate::setMostVotedPosition(const int &mostVotedPosition) {
    this->mostVotedPosition = mostVotedPosition;
}

// muito feio!!
tm parseDate(const string& dateStr) {
    tm date = {};
    istringstream ss(dateStr);
    char sep1, sep2;

    ss >> date.tm_mday >> sep1 >> date.tm_mon >> sep2 >> date.tm_year;
    if (ss.fail() || sep1 != '/' || sep2 != '/') {
        throw runtime_error("Formato de data inválido.");
    }

    date.tm_mon -= 1;
    date.tm_year -= 1900;

    return date;
}