package com.sistemaescolar.repository;

import com.sistemaescolar.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("""
        select distinct a
        from Aluno a
        left join Matricula m on m.aluno.id = a.id
        where (:busca is null or lower(a.nome) like lower(concat('%', :busca, '%')))
          and (:status is null or a.status = :status)
          and (:escolaId is null or a.escola.id = :escolaId)
          and (:turmaId is null or m.turma.id = :turmaId)
    """)
    Page<Aluno> filtrarAlunos(
            @Param("busca") String busca,
            @Param("status") String status,
            @Param("escolaId") Long escolaId,
            @Param("turmaId") Long turmaId,
            Pageable pageable
    );

    long countByEscola_Id(Long escolaId);
}