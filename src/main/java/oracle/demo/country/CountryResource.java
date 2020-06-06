package oracle.demo.country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterStyle;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * OpenAPI demo
 */
@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private CountryService countryService = new CountryService();

    public CountryResource() {
    }

    @Operation(summary = "List all countries", description = "保持している全ての国情報のリストを取得します")
    @APIResponses({ 
            @APIResponse(
                responseCode = "200", description = "国情報のリスト", 
                content = {@Content(
                                mediaType = "application/json", 
                                schema = @Schema(type = SchemaType.ARRAY, implementation = Country.class)
                            )}
                ),
            @APIResponse(responseCode = "401", description = "認証に失敗しました") 
        })
    @GET
    @Path("/")
    public Country[] getCountries() throws Exception {
        return countryService.getCountries();
    }

    @Operation(summary = "Find country by country code", description = "国コードから国情報を検索します")
    @APIResponses({ 
            @APIResponse(
                responseCode = "200", description = "国情報", 
                content = {@Content(
                                mediaType = "application/json", 
                                schema = @Schema(type = SchemaType.OBJECT, implementation = Country.class)
                            )}
                ),
            @APIResponse(responseCode = "401", description = "認証に失敗しました"),
            @APIResponse(responseCode = "404", description = "指定した国コードから国情報が見つかりませんでした") 
        })
    @GET
    @Path("/{countryId}")
    public Country getCountry(
                    @Parameter(
                        name = "countryId", 
                        description = "国際電話の国番号 - US=1, JP=81, etc.", 
                        style = ParameterStyle.DEFAULT, 
                        required = true
                        ) 
                    @PathParam("countryId") 
                    int countryId) {
        return countryService.getCountry(countryId);
    }

    public static class CountryService {
        private static Map<Integer, String> countries;// = new HashMap<Integer, String>();
        static {
            countries = new HashMap<Integer, String>();
            countries.put(1, "USA");
            countries.put(81, "Japan");
        }

        public Country getCountry(int countryId) {
            String countryName = countries.get(countryId);
            Optional.ofNullable(countryName)
                    .orElseThrow(() -> new CountryNotFoundException(String.format("Bad countryId: %d", countryId)));
            return new Country(countryId, countryName);
        }

        public Country[] getCountries() {
            ArrayList<Country> cList = new ArrayList<Country>();
            countries.forEach((id, name) -> {
                cList.add(new Country(id, name));
            });
            return cList.toArray(new Country[countries.size()]);
        }

    }

    public static class Country {

        public int countryId;
        public String countryName;

        public Country() {
        } // need empty constructor

        public Country(int countryId, String countryName) {
            this.countryId = countryId;
            this.countryName = countryName;
        }
    }

}
