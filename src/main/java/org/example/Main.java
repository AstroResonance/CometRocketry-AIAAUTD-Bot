package org.example;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class Main {
    static final String SPREADSHEET_ID = "1-EqQKArLu65S2inRPunV4OSpDpL5uucec1fTfd9k-NQ";
    static final int SPREADSHEETGID = 1319273;
    public static void main(String[] args) throws GeneralSecurityException, IOException, InterruptedException {
        final String token;

        try (InputStream stream = Main.class.getResourceAsStream("/discord-token.txt")) {
            token = new String(Objects.requireNonNull(stream).readAllBytes(), StandardCharsets.UTF_8);
        }
        final JDA jda = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("How to become a sentient AI"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        jda.awaitReady();

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, SheetsQuickstart.JSON_FACTORY, SheetsQuickstart.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(SheetsQuickstart.APPLICATION_NAME)
                        .build();
        while(true) {
            List<RocketryEntry> rocketryEntries = rocketryList(service);
            discordSide(rocketryEntries, jda);
            deleteDuplicates(service, memberDuplicates(rocketryEntries));
            Thread.sleep(1000);
        }
    }
    public static void discordSide(final List<RocketryEntry> rocketryEntries, JDA jda) throws InterruptedException, IOException {

        // Read text from stream and make string variable
        long guildId = 708421411693264926L;
        Guild guild = jda.getGuildById(guildId);
        Role role = guild.getRoleById(1124185933684687001L);
        memberDuplicates(rocketryEntries);
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


    public static void deleteDuplicates(Sheets service, int i){
        if(i != -1){
            final BatchUpdateSpreadsheetRequest content = new BatchUpdateSpreadsheetRequest();
            final Request request = new Request()
                    .setDeleteDimension(new DeleteDimensionRequest()
                            .setRange(new DimensionRange()
                                    .setSheetId(SPREADSHEETGID)
                                    .setDimension("ROWS")
                                    .setStartIndex(i + 2)
                                    .setEndIndex(i + 3)
                            )
                    );

            request.setDeleteDimension(new DeleteDimensionRequest().setRange(new DimensionRange().setDimension("ROWS").setStartIndex(i + 2).setEndIndex(i + 3).setSheetId(SPREADSHEETGID)));

            final List<Request> requests = new ArrayList<>();
            requests.add(request);
            content.setRequests(requests);

            try {
                service.spreadsheets().batchUpdate(SPREADSHEET_ID, content).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("No duplicates");
        }
    }



    public static int memberDuplicates(final List<RocketryEntry> rocketryEntries) {
        System.out.println("Entering Member Duplicates");
        for (int i = 0; i < rocketryEntries.size(); i++) {
            for (int j = i + 1; j < rocketryEntries.size(); j++) {
                if (rocketryEntries.get(i).getName().equals(rocketryEntries.get(j).getName())) {
                    System.out.println(rocketryEntries.get(j).getName() + " with discord username: ");
                    System.out.println(rocketryEntries.get(j).getDiscordUsername() + " is a duplicate");
                    System.out.println(i + " " + j);
                    return i;
                }
            }
        }
        return -1;
    }


    public static List<RocketryEntry> rocketryList(final Sheets service) throws IOException {

        final String range = "A3:E";
        List<RocketryEntry> rocketryEntries = new ArrayList<>();
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {

            for (final List<Object> row : values) {
                final String meetingCount = (String) row.get(0);
                final String timestamp = (String) row.get(1);
                final String name = (String) row.get(2);
                final String discord = (String) row.get(3);
                final String color = (String) row.get(4);
                System.out.printf("%s, %s, %s, %s, %s \n", row.get(0), row.get(1), row.get(2), row.get(3), row.get(4));
                rocketryEntries.add(new RocketryEntry(meetingCount,timestamp, name, discord, color));
            }
        }
        return rocketryEntries;
    }
}
