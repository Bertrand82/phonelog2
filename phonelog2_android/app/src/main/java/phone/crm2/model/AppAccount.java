package phone.crm2.model;

import java.security.MessageDigest;

public class AppAccount {

    private long id;
    private String mail;
    private String password;

    public AppAccount() {
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
   /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
   /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }



    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }



    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }



    /**
     * @param password the password to set
     */
    public void setPassword(String password) {

        try {
            this.password = sha512(password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * @author https://github.com/whymarrh/passvault/blob/master/Passvault.java#L65-L80
     * Returns the SHA-512 hashcode of the given string.
     * @param s the string to be hashed.
     * @return the SHA-512 hashcode of {@code s}.
     */
    private String sha512(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(s.getBytes());
        byte[] bytes = md.digest();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
            buffer.append(tmp);
        }
        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AppAccount [id=" + id + ", mail=" + mail + "]";
    }






    public String getcryptPhrase() {
        // TODO prototype methode utile pour upload historic
        return "";
    }




}
