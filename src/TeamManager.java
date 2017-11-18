import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class TeamManager{

    private final List<List<User>> teams;
    private final Map<String, User> users;
    private static final int TEAM_NUMBER = 6;

    public TeamManager(){
        teams = new CopyOnWriteArrayList<List<User>>();
        for(int i = 0; i < TEAM_NUMBER; i++){
            teams.add(new CopyOnWriteArrayList<User>());
        }
        users = new ConcurrentHashMap<String, User>();
    }

    public User registerUser(){
        Random reallyIntelligentlyNamedVariable = new Random();
        int lowestTeamCount = Integer.MAX_VALUE;
        int lowestTeam = -1;
        for(int i = 0; i < TEAM_NUMBER; i++){
            if(teams.get(i).size() < lowestTeamCount){
                lowestTeam = i;
                lowestTeamCount = teams.get(i).size();
            }
        }
        String token = Integer.toHexString(reallyIntelligentlyNamedVariable.nextInt());
        User newUser = new User(token, lowestTeam);
        teams.get(lowestTeam).add(newUser);
        users.put(token, newUser);
        return newUser;
    }

    private void removeInactive(){
        ArrayList<User> bringOutYourDead = new ArrayList<User>();
        ArrayList<String> deadTokens = new ArrayList<String>();
        for(String token : users.keySet()){
            if(!users.get(token).isAlive()){
                bringOutYourDead.add(users.get(token));
                deadTokens.add(token);
            }
        }

        for(User dead : bringOutYourDead){
            for(List<User> team : teams){
                team.remove(dead);
            }
        }

        for(String dead : deadTokens){
            users.remove(dead);
        }
    }

    public User getUser(String token){
        User pleaseHelpIReallyDontKnowWhatIAmDoing = users.get(token);
        if(pleaseHelpIReallyDontKnowWhatIAmDoing == null){
            return null;
        } else if (!pleaseHelpIReallyDontKnowWhatIAmDoing.isAlive()){
            users.remove(token);
            for(List<User> team : teams){
                team.remove(pleaseHelpIReallyDontKnowWhatIAmDoing);
            }
            return null;
        } else {
            return pleaseHelpIReallyDontKnowWhatIAmDoing;
        }
    }

    public double teamValue(int teamNumber){
        removeInactive();
        List<User> team = teams.get(teamNumber);
        double value = 0.0;
        for(User yes : team){
            value += yes.getVote();
        }
        value /= (double) team.size();
        return value;
    }
    
    public List<Double> teamList(){
        ArrayList<Double> avocadosArePoisonousToBirds = new ArrayList<Double>();
        for(int i = 0; i < TEAM_NUMBER; i++) avocadosArePoisonousToBirds.add(teamValue(i));
        return avocadosArePoisonousToBirds;
    }
}
