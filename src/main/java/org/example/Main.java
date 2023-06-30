package org.example;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        discordSide(rocketryList());

    }
    public static void discordSide(final List<RocketryEntry> rocketryEntries) throws InterruptedException {
        JDA jda = JDABuilder.createDefault("MTExMzU1MjE2NjQ1NjY3NjQ3Mw.GicYLO.rzOOfrm_v6rrR_XXItOiUXRMywHhZ13Fq2Hx-I")
                .setActivity(Activity.watching("How to become a sentient AI"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        jda.awaitReady();
        long guildId = 708421411693264926L;
        Guild guild = jda.getGuildById(guildId);
        long roleId = 1113537105067524146L;
        Role role = guild.getRoleById(roleId);
        for (RocketryEntry rocketryEntry : rocketryEntries) {
            System.out.print(rocketryEntry.getDiscordUsername() + "  ");
            System.out.println(rocketryEntry.getName());
            List<Member> members = guild.retrieveMembersByPrefix(rocketryEntry.getName(), 1).get();
            if (members.size() > 0) {
                Member member = members.get(0);
                guild.addRoleToMember(member, role).queue();
            }
        }
    }

    public static List<RocketryEntry> rocketryList() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1-EqQKArLu65S2inRPunV4OSpDpL5uucec1fTfd9k-NQ";
        final String range = "A3:D";
        List<RocketryEntry> rocketryEntries = new ArrayList<>();
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, SheetsQuickstart.JSON_FACTORY, SheetsQuickstart.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(SheetsQuickstart.APPLICATION_NAME)
                        .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {

            for (List<Object> row : values) {
                String timestamp = (String) row.get(0);
                String name = (String) row.get(1);
                String discord = (String) row.get(2);
                String color = (String) row.get(3);
                System.out.printf("%s, %s, %s, %s \n", row.get(0), row.get(1), row.get(2), row.get(3));
                rocketryEntries.add(new RocketryEntry(timestamp, name, discord, color));
            }
        }
        return rocketryEntries;
    }
}
