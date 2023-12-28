package somepackage;

import com.github.object.persistence.session.Session;
import com.github.object.persistence.session.SessionProvider;

public class Main {
    public static void main(String[] args) {
        try (Session session = SessionProvider.getInstance().createSession()) {
            System.out.println(session.createTable(Table.class).get());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try (Session session = SessionProvider.getInstance().createSession()) {
            System.out.println(session.createTable(Table.class).get());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        SessionProvider.getInstance().shutdown();
    }
}
