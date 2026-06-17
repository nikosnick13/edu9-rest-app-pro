package gr.aueb.cf.eduapp.specification;

import gr.aueb.cf.eduapp.core.filters.TeacherFilters;
import gr.aueb.cf.eduapp.model.Teacher;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification {

    public static Specification<Teacher> build(TeacherFilters fileters){
        return Specification.allOf(
                hasLastname(fileters.getLastname()),
                hasRegion(fileters.getRegion()),
                isDelete(fileters.isDelete())
        );
    }

    private static Specification<Teacher> hasLastname(String lastname){
        return ((root, query, cb) ->
                cb.like(cb.lower(root.get("lastname")), lastname.toLowerCase() + "%"));
    }

    private static Specification<Teacher> hasRegion(String region){
        return ((root, query, cb) -> region ==null ? cb.conjunction() :
                cb.equal(cb.lower(root.get("region").get("name")), region.toLowerCase()));
    }

    private static Specification<Teacher> isDelete(boolean delete){
        return ((root, query, cb) ->
                cb.equal(root.get("delete"),delete));
    }
}
