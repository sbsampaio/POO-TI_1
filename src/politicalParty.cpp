#include "politicalParty.hpp"

PoliticalParty::PoliticalParty(const vector<string> &values)
{

}

string PoliticalParty::getName()
{
    return name;
}

string PoliticalParty::getPartyAcronym()
{
    return acronym;
}

int PoliticalParty::getNumber()
{
    return number;
}

int PoliticalParty::getNominalVotes()
{
    return nominalVotes;
}

int PoliticalParty::getCaptionVotes()
{
    return captionVotes;
}

list<Candidate> PoliticalParty::getCandidates()
{
    return candidates;
}

int PoliticalParty::getElectedCandidates()
{
    return electedCandidates;
}

int PoliticalParty::getTotalVotes()
{
    return nominalVotes + captionVotes;
}

void PoliticalParty::addNominalVotes(const int &votes)
{
    nominalVotes += votes;
}

void PoliticalParty::addCaptionVotes(const int &votes)
{
}

void PoliticalParty::addCandidate(const Candidate &candidate)
{
    candidates.push_back(candidate);
}

Candidate PoliticalParty::getMostVotedCandidate()
{
    // o mais votado deve  ficar no inicio da fila
    return candidates.front(); 
}

Candidate PoliticalParty::getLeastVotedCandidate()
{
    // o menos votado deve ficar no final da fila
    return candidates.back();
}

Candidate PoliticalParty::getLeastVotedElectedCandidate()
{
}

void PoliticalParty::computeElectedCandidates()
{
}

map<int, PoliticalParty> PoliticalParty::readPoliticalParties(const string &filePath)
{
}