package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.Project;
import pl.skrys.dao.ProjectRepository;

import java.util.List;

@Service("projectService")
public class ProjectService{


    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void addProject(Project project) {

        projectRepository.save(project);
    }

    @Transactional
    public void editProject(Project project) {

        projectRepository.save(project);
    }

    @Transactional
    public List<Project> listProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public Project getProject(long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project getProjectByName(String name) {
        return projectRepository.findByNazwa(name);
    }

    @Transactional
    public void removeProject(long id) {
        projectRepository.delete(id);
    }
}
